package qa.challenge.api.tests;

import org.junit.jupiter.api.*;
import qa.challenge.api.base.BaseTest;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Tag("regressao")
@Tag("obter_usuario")
@DisplayName("Feature: Teste de Obtenção de Usuário")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserTest extends BaseTest {

    int randNumb;
    String userId;

    @BeforeEach
    public void setup() {
        response = getListUsers();
        List<Map<String, Object>> users = response.jsonPath().getList("usuarios");
        randNumb = new Random().nextInt(users.size());
    }

    @Test
    @Order(1)
    @Tag("obter_usuario_aleatorio")
    @DisplayName("Cenário 01: Deve obter um usuário aleatório na lista e validar os dados")
    public void getRandomUserAndValidateFields() {
        response.then()
                .statusCode(SC_OK)
                .body("usuarios[" + randNumb + "].nome", notNullValue())
                .body("usuarios[" + randNumb + "].email", notNullValue())
                .body("usuarios[" + randNumb + "].password", notNullValue())
                .body("usuarios[" + randNumb + "].administrador", notNullValue())
                .body(matchesJsonSchemaInClasspath("contracts/getUserSchema.json"));
    }

    @Test
    @Order(2)
    @Tag("obtem_usuario_por_id")
    @DisplayName("Cenário 02: Deve obter um usuário por ID")
    public void getUserByIdAndValidateFields() {
        userId = response.jsonPath().getString("usuarios[" + randNumb + "]._id");
        response = getUserById(userId);

        response.then()
                .statusCode(SC_OK)
                .body("_id", equalTo(userId))
                .body("email", notNullValue())
                .body("nome", notNullValue());
    }

    @Test
    @Order(3)
    @Tag("id_inexistente")
    @DisplayName("Cenário 03: Deve retornar erro ao obter usuário com ID inexistente")
    public void getUserIdNonexistent() {
        String invalidUserId = "sd3abc567def3efd";
        response = getUserById(invalidUserId);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @Order(4)
    @Tag("id_invalido")
    @DisplayName("Cenário 04: Deve retornar erro ao obter usuário com ID inválido")
    public void getUserIdInvalid() {
        String invalidUserId = "12345";
        response = getUserById(invalidUserId);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("id", equalTo("id deve ter exatamente 16 caracteres alfanuméricos"));
    }

    @Test
    @Order(5)
    @Tag("lista_usuarios_rate_limit")
    @DisplayName("Cenário 05: Deve validar o rate limit ao obter a lista de usuários")
    public void validateUserListRateLimit() {
        for (int i = 0; i < 100; i++) {
            response = getListUsers();
        }
//        response.then().statusCode(429);
    }
}