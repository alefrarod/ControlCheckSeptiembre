package acme.testing.epicure.pimpam;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class EpicurePimpamDeleteTest extends TestHarness{
	
	@ParameterizedTest
	@CsvFileSource(resources = "/epicure/pimpam/create.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void delete(final int recordIndex, final String code, final String request, final String budget,
		final String initialDate, final String finishDate, final String url) {
		super.signIn("epicure1", "epicure1");
		super.clickOnMenu("Epicure", "Pimpam");
		super.checkListingExists();
		
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.checkNotErrorsExist();

		super.signOut();
	}

}
