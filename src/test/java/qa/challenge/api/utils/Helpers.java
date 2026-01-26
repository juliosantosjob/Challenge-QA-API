package qa.challenge.api.utils;

import qa.challenge.api.base.BaseTest;
import qa.challenge.api.models.NewUsersModel;

public class Helpers extends BaseTest {

    /** Gera um objeto NewUsersModel com dados randomicos
     *
     * @param isAdmin Define se o usuário é administrador ou não
     * @return NewUsersModel com dados randomicos
     */

    public static NewUsersModel getRandomUser(boolean isAdmin) {
        logger.info("Obtendo usuário randomico. Admin: {}", isAdmin);

        NewUsersModel user = new NewUsersModel();
        user.setNome(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        user.setAdministrador(String.valueOf(isAdmin));

        return user;
    }

    /** Obtém o ID do usuário a partir do email
     *
     * @param user Objeto NewUsersModel com os dados do usuário
     * @return ID do usuário
     */

    public static String getUserId(NewUsersModel user) {
        logger.info("Obtendo ID do usuário: {}", user.getEmail());

        return getUser(user)
                .then()
                .extract()
                .path("usuarios[0]._id");
    }
}