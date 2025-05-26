import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f(0f, 0f, 3f);
    public Vector3f front = new Vector3f(0f, 0f, -1f);
    public Vector3f up = new Vector3f(0f, 1f, 0f);

    private Vector3f right = new Vector3f();
    private Vector3f worldUp = new Vector3f(0f, 1f, 0f);

    private float yaw = -90.0f;
    private float pitch = 0.0f;
    private float lastX = 400, lastY = 300;
    private boolean firstMouse = true;

    public Matrix4f getViewMatrix() {
        Vector3f center = new Vector3f(position).add(front);
        return new Matrix4f().lookAt(position, center, up);
    }

    public Matrix4f getProjectionMatrix(float aspect) {
        return new Matrix4f().perspective((float) Math.toRadians(45.0f), aspect, 0.1f, 100.0f);
    }

    public void processKeyboard(String direction, float speed) {
        Vector3f move = new Vector3f();
        switch (direction) {
            case "FORWARD" -> front.mul(speed, move);
            case "BACKWARD" -> front.mul(-speed, move);
            case "LEFT" -> right.mul(-speed, move);
            case "RIGHT" -> right.mul(speed, move);
        }
        position.add(move);
    }

    public void processMouseMovement(float xpos, float ypos) {
        if (firstMouse) {
            lastX = xpos;
            lastY = ypos;
            firstMouse = false;
        }

        float xoffset = xpos - lastX;
        float yoffset = lastY - ypos; // y는 반대방향
        lastX = xpos;
        lastY = ypos;

        float sensitivity = 0.1f;
        xoffset *= sensitivity;
        yoffset *= sensitivity;

        yaw += xoffset;
        pitch += yoffset;

        if (pitch > 89.0f) pitch = 89.0f;
        if (pitch < -89.0f) pitch = -89.0f;

        updateCameraVectors();
    }

    private void updateCameraVectors() {
        Vector3f direction = new Vector3f();
        direction.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        direction.y = (float) Math.sin(Math.toRadians(pitch));
        direction.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front = direction.normalize();

        right = new Vector3f();
        front.cross(worldUp, right).normalize();
        up = new Vector3f();
        right.cross(front, up).normalize();
    }
}
