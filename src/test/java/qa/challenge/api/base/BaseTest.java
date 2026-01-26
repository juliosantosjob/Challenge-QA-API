package qa.challenge.api.base;

import com.github.javafaker.Faker;
import qa.challenge.api.models.LoginModel;
import qa.challenge.api.models.NewUsersModel;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import static qa.challenge.api.utils.ConfigProp.getSecret;
import static qa.challenge.api.utils.Reports.attachmentsAllure;

public class BaseTest implements Constants {
    protected static final Faker faker = new Faker();
    protected static final Logger logger = LogManager.getLogger();
    protected String BASE_URL = getSecret("BASE_URL");
    protected Response response;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        if (BASE_URL == null) {
            logger.error("BASE_URL não encontrada. Configure no config.properties (src/test/resources) " +
                    "como BASE_URL=https://serverest.dev/ ou defina como variável de ambiente.");
        }

        logger.info("Iniciando setup RestAssured");
        logger.info(testInfo.getDisplayName());

        ResponseSpecBuilder responseSpec = new ResponseSpecBuilder();

        responseSpec.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        RestAssured.baseURI = BASE_URL;
        RestAssured.responseSpecification = responseSpec.build();
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", CONTENT_TYPE).build();

        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @AfterEach
    public void endSetup() {
        attachmentsAllure(response);
        logger.info("Status da chamada: {}", response.getStatusCode() + "\n");
    }

    /**
     * Mwtodo para obter a lista de usuários
     *
     * @return Response da chamada
     */

    public static Response getListUsers() {
        logger.info("Iniciando requisição para obter lista de usuários");
        try {
            return RestAssured
                    .given()
                    .get(USERS);

        } catch (Exception e) {
            logger.error("Erro ao obter lista de usuários: {}", e.getMessage());
            throw new RuntimeException("Failed to get users: " + e.getMessage());
        }
    }

    /**
     * Mwtodo para obter um usuário por email e senha
     *
     * @param user Objeto contendo email e senha do usuário
     * @return Response da chamada
     */

    public static Response getUser(NewUsersModel user) {
        logger.info("Iniciando requisição para obter usuário: {}", user.getEmail());
        try {
            return RestAssured
                    .given()
                    .queryParam("nome", user.getNome())
                    .queryParam("email", user.getEmail())
                    .queryParam("password", user.getPassword())
                    .queryParam("administrador", user.getAdministrador())
                    .when()
                    .get(USERS);

        } catch (Exception e) {
            logger.error("Erro ao obter usuário: {}", e.getMessage());
            throw new RuntimeException("Failed to get users: " + e.getMessage(), e);
        }
    }

    /**
     * Metodo para obter um usuário por ID
     *
     * @param idUser Id do usuário que será obtido
     * @return Response da chamada
     */

    public static Response getUserById(String idUser) {
        logger.info("Iniciando requisição para obter usuário com ID: {}", idUser);
        try {
            return RestAssured
                    .given()
                    .when()
                    .get(USERS.concat(idUser));

        } catch (Exception e) {
            logger.error("Erro ao obter usuário por ID: {}", e.getMessage());
            throw new RuntimeException("Failed to get user by ID: " + e.getMessage(), e);
        }
    }

    /**
     * Metodo para realizar login de um usuário
     *
     * @param credentials Objeto contendo as credenciais de login (email e senha)
     * @return Response da chamada
     */

    public static Response loginUser(LoginModel credentials) {
        logger.info("Iniciando login para usuário: {}", credentials.getEmail());
        try {
            return RestAssured
                    .given()
                    .body(credentials)
                    .when()
                    .post(LOGIN);

        } catch (Exception e) {
            logger.error("Erro ao realizar login: {}", e.getMessage());
            throw new RuntimeException("Error logging in: " + e.getMessage());
        }
    }

    /**
     * Metodo para editar usuarios
     *
     * @param newUser Objeto java com os dados do usuario
     * @param idUser  Id da conta do usuario para realizar a edição
     * @return Response da chamada
     */

    public static Response editUser(NewUsersModel newUser, String idUser) {
        try {
            return RestAssured
                    .given()
                    .body(newUser)
                    .when()
                    .put(USERS.concat(idUser));

        } catch (Exception e) {
            logger.error("Erro ao editar usuário: {}", e.getMessage());
            throw new RuntimeException("Failed to edit user" + e.getMessage());
        }
    }

    /**
     * Metodo para registrar um novo usuário
     *
     * @param newUser Objeto contendo os dados do novo usuário
     * @return Response da chamada
     */

    public static Response registerUser(NewUsersModel newUser) {
        logger.info("Iniciando cadastro de novo usuário: {}", newUser.getEmail());
        try {
            return RestAssured
                    .given()
                    .body(newUser)
                    .when()
                    .post(USERS);

        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário: {}", e.getMessage());
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }

    /**
     * Metodo para deletar usuarios
     *
     * @param idUser Id da conta que será deletada
     */

    public static Response deleteUser(String idUser) {
        logger.info("Iniciando exclusão do usuário com ID: {}", idUser);
        try {
            return RestAssured
                    .given()
                    .when()
                    .delete(USERS.concat(idUser));

        } catch (Exception e) {
            logger.error("Erro ao deletar usuário: {}", e.getMessage());
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }

}