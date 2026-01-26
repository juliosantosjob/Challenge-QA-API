package qa.challenge.api.runners;

import qa.challenge.api.tests.EditUserTest;
import qa.challenge.api.tests.GetUserTest;
import qa.challenge.api.tests.LoginUserTest;
import qa.challenge.api.tests.RegisterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags("regressao")
@DisplayName("Automacao de Servicos - ServeRest")
@SelectPackages("src.test.java.qa.challenge.api.tests")
@SelectClasses({
        LoginUserTest.class,
        RegisterTest.class,
        EditUserTest.class,
        GetUserTest.class
})
public class TestRunner { }