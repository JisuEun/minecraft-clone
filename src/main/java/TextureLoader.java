import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBImage;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {
    public static int loadTexture(String path) {
        int width, height;
        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);

            // 리소스를 InputStream으로 불러오기
            InputStream input = TextureLoader.class.getClassLoader().getResourceAsStream(path);
            if (input == null) throw new RuntimeException("리소스 파일 찾을 수 없음: " + path);

            byte[] bytes = input.readAllBytes();
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes).flip();

            image = stbi_load_from_memory(buffer, w, h, comp, 4);
            if (image == null) throw new RuntimeException("텍스처 디코딩 실패: " + path);

            width = w.get();
            height = h.get();
        } catch (Exception e) {
            throw new RuntimeException("텍스처 로딩 실패: " + path, e);
        }

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(image);

        return textureID;


    }
}
