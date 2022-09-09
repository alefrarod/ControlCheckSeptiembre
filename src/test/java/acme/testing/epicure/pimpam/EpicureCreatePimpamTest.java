package acme.testing.epicure.pimpam;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class EpicureCreatePimpamTest extends TestHarness{
	
	@ParameterizedTest
	@CsvFileSource(resources = "/epicure/pimpam/create.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positive(final int recordIndex,final String code, final String title, final String description,
		final String startPeriod, final String finishPeriod , final String budget, final String link) {
		super.signIn("epicure1", "epicure1");
		super.clickOnMenu("Epicure", "Pimpam");
		super.checkListingExists();
		
		super.clickOnButton("Create");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("budget", budget);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.checkNotErrorsExist();

		super.signOut();
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/epicure/pimpam/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void negative(final int recordIndex,final String code, final String title, final String description,
			final String startPeriod, final String finishPeriod , final String budget, final String link) {
		super.signIn("epicure1", "epicure1");
		super.clickOnMenu("Epicure", "Pimpam");
		super.checkListingExists();
		
		super.clickOnButton("Create");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("budget", budget);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

}
