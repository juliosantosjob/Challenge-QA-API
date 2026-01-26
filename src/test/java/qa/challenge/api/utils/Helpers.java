package qa.challenge.api.utils;

import qa.challenge.api.base.BaseTest;
import qa.challenge.api.models.NewUsersModel;

public class Helpers extends BaseTest {

    public static NewUsersModel getRandomUser(boolean isAdmin) {
        logger.info("Obtendo usuário randomico. Admin: {}", isAdmin);

        NewUsersModel user = new NewUsersModel();
        user.setNome(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        user.setAdministrador(String.valueOf(isAdmin));

        return user;
    }

    public static String getUserId(NewUsersModel user) {
        logger.info("Obtendo ID do usuário: {}", user.getEmail());

        return getUser(user)
                .then()
                .extract()
                .path("usuarios[0]._id");
    }
}