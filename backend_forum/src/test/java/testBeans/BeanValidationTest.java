package beans;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.EqualsAndHashCodeMatchRule;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BeanValidationTest {

    private static final String PACKAGE_BEANS = "fr.dsfr.forum.beans";
    private static final String PACKAGE_DTOS = "fr.dsfr.forum.beans.dto";

    @Test
    public void validerTousLesBeans() {
        valider(PACKAGE_BEANS);
        valider(PACKAGE_DTOS);
    }

    private void valider(String packageName) {
        List<PojoClass> classes = PojoClassFactory.getPojoClassesRecursively(packageName, new FilterPackageInfo());

        System.out.println("🧪 Classes testées dans le package " + packageName + " :");
        classes.forEach(c -> System.out.println(" - " + c.getName()));

        Validator validator = ValidatorBuilder.create()
                // Test des getters et setters
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new GetterTester())
                .with(new SetterTester())
                // Test de la méthode equals et hashCode
                .with(new EqualsAndHashCodeMatchRule())
                .build();

        validator.validate(classes);
    }
}
