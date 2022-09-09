package acme.testing.chef.delor;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class ChefCreateDelorTest extends TestHarness{
	
	@ParameterizedTest
	@CsvFileSource(resources = "/chef/delor/create.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positive(final int recordIndex,final String keylet, final String subject, final String explanation,
		final String startPeriod, final String finishPeriod , final String income, final String moreInfo) {
		super.signIn("chef1", "chef1");
		super.clickOnMenu("Chef", "Delor");
		super.checkListingExists();
		
		super.clickOnButton("Create");
		super.fillInputBoxIn("keylet", keylet);
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("explanation", explanation);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("income", income);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.clickOnSubmit("Create");

		super.checkNotErrorsExist();

		super.signOut();
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/chef/delor/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void negative(final int recordIndex,final String keylet, final String subject, final String explanation,
			final String startPeriod, final String finishPeriod , final String income, final String moreInfo) {
		super.signIn("chef1", "chef1");
		super.clickOnMenu("Chef", "Delor");
		super.checkListingExists();
		
		super.clickOnButton("Create");
		super.fillInputBoxIn("keylet", keylet);
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("explanation", explanation);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("income", income);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

}
