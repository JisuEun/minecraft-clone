import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    public Vector3f position;
    public Vector3f front;
    public Vector3f up;

    public Camera() {
        position = new Vector3f(0.0f, 0.0f, 3.0f);
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        up = new Vector3f(0.0f, 1.0f, 0.0f);
    }

    public Matrix4f getViewMatrix() {
        Vector3f center = new Vector3f();
        position.add(front, center);
        return new Matrix4f().lookAt(position, center, up);
    }

    public Matrix4f getProjectionMatrix(float aspectRatio) {
        return new Matrix4f().perspective((float) Math.toRadians(45.0f), aspectRatio, 0.1f, 100.0f);
    }
}
