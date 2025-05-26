import java.io.IOException;

public class ShaderUtils {
    public static String loadAsString(String resourcePath) {
        try (var input = ShaderUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new RuntimeException("셰이더 파일을 찾을 수 없습니다: " + resourcePath);
            }
            return new String(input.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("셰이더 로딩 실패: " + resourcePath, e);
        }
    }
}
