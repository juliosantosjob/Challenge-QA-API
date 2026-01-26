package qa.challenge.api.tests;

import org.junit.jupiter.api.*;
import qa.challenge.api.base.BaseTest;
import qa.challenge.api.models.NewUsersModel;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static qa.challenge.api.utils.Helpers.getRandomUser;
import static qa.challenge.api.utils.Helpers.getUserId;

@Tag("regressao")
@Tag("delete_user_regressao")
@DisplayName("Feature: Testes de Deleção de Usuário")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteUserTest extends BaseTest {

    NewUsersModel dynamicUser_;
    String id_;

    @BeforeEach
    public void setUp() {
        dynamicUser_ = getRandomUser(true);
    }

    @Test
    @Order(1)
    @Tag("deletar_usuario_sucesso")
    @DisplayName("Cenario 01: Deve realizar delecao com sucesso")
    public void deleteUserSuccessful() {
        response = registerUser(dynamicUser_);

        String id_ = getUserId(dynamicUser_);
        deleteUser(id_)
                .then()
                .statusCode(SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"))
                .body(matchesJsonSchemaInClasspath("contracts/deleteUser/delete-user-success.json"));
    }

    @Test
    @Order(2)
    @Tag("id_inexistente")
    @DisplayName("Cenario 02: Deve falhar ao tentar deletar usuario sem informar ID")
    public void deleteUserWithoutId() {
        id_ = "";

        response = registerUser(dynamicUser_);
        deleteUser(id_)
                .then()
                .statusCode(SC_METHOD_NOT_ALLOWED)
                .body("message", equalTo(
                        "Não é possível realizar DELETE em /usuarios/. Acesse https://serverest.dev para ver as rotas disponíveis e como utilizá-las."
                )).body(matchesJsonSchemaInClasspath("contracts/deleteUser/delete-user-error.json"));

        id_ = getUserId(dynamicUser_);
        deleteUser(id_);
    }

    @Test
    @Order(3)
    @Tag("id_invalido")
    @DisplayName("Cenario 03: Deve falhar ao tentar deletar usuario com ID invalido")
    public void deleteUserWithInvalidId() {
        id_ = "invalid_id_123";

        response = registerUser(dynamicUser_);
        deleteUser(id_)
                .then()
                .statusCode(SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));

        id_ = getUserId(dynamicUser_);
        deleteUser(id_);
    }
}
