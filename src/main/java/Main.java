// glfw: 창, 입력(마우스, 키보드) 처리
// opengl: 그래픽 렌더링
// system: 메모리 관련 처리

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer; // 메모리 버퍼 (C 스타일 포인터)

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private long window; // 창을 나타내는 핸들. long 타입의 ID

    public void run() {
        System.out.println("Hello LWJGL!");

        init();
        loop();

        // 정리
        glfwFreeCallbacks(window); // 콜백 제거
        glfwDestroyWindow(window); // 창 제거
        glfwTerminate(); // GLFW 종료
        glfwSetErrorCallback(null).free(); // 에러 콜백 제거
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set(); // 오류 발생 시 콘솔에 출력

        if (!glfwInit()) // GLFW 초기화
            throw new IllegalStateException("GLFW 초기화 실패");

        glfwDefaultWindowHints(); // 기본 창 옵션 설정
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // 창을 처음엔 숨김 상태로 생성
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // 창 크기 조절

        // 실제 창 생성 및 핸들값 window에 저장
        window = glfwCreateWindow(800, 600, "Hello OpenGL", NULL, NULL);
        if (window == NULL) // 창 생성 실패했는지 확인
            throw new RuntimeException("창 생성 실패");

        // 창 화면 가운데에 위치시키기
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // 너비 저장 공간 확보
            IntBuffer pHeight = stack.mallocInt(1); // 높이 저장 공간 확보

            glfwGetWindowSize(window, pWidth, pHeight); // 창 크기 가져오기
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); // 모니터 해상도 정보

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            ); // 화면 정중앙에 창 배치
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities(); // OpenGL 함수들 사용 가능하게 만듦

        // OpenGL을 이 창에서 쓸 수 있게 지정
        glfwSwapInterval(1); // V-Sync 활성화 (화면 찢김 방지)
        glfwShowWindow(window); // 숨겨진 창을 보여줌
    }

    private void loop() {
        int shaderProgram;

        // 정점 좌표 (삼각형)
        float[] vertices = {
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.0f,
                0.0f, 0.5f, 0.0f
        };

        // 버퍼 생성
        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);

        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        // 셰이더 컴파일 및 링크
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, ShaderUtils.loadAsString("shaders/vertex.glsl"));
        GL20.glCompileShader(vertexShader);

        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShader, ShaderUtils.loadAsString("shaders/fragment.glsl"));
        GL20.glCompileShader(fragmentShader);

        shaderProgram = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);
        GL20.glLinkProgram(shaderProgram);

        // 셰이더는 연결 후 삭제 가능
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        while (!glfwWindowShouldClose(window)) { // 창을 닫지 않으면 계속 실행
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // 화면 초기화
            glClearColor(0.1f, 0.2f, 0.3f, 1.0f); // 배경색 파란색으로 설정

            GL20.glUseProgram(shaderProgram);
            GL30.glBindVertexArray(vao);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
            GL30.glBindVertexArray(0);

            glfwSwapBuffers(window); // 화면 갱신
            glfwPollEvents(); // 키보드, 마우스 등 이벤트 처리
        }
    }

    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (Exception e) {
            e.printStackTrace(); // ❗ 실제 에러 로그 출력
        }
    }
}
