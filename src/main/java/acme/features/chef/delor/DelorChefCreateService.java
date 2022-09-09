package acme.features.chef.delor;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.artifact.Artifact;
import acme.entities.artifact.ArtifactType;
import acme.entities.delor.Delor;
import acme.entities.systemSetting.SystemSettings;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractCreateService;
import acme.roles.Chef;

@Service
public class DelorChefCreateService implements AbstractCreateService<Chef, Delor>{
	
	@Autowired
	protected DelorRepository repository;
		
	// AbstractCreateService<Patron, Patronage> interface ---------------------
	
	@Override
	public boolean authorise(final Request<Delor> request) {
		assert request != null;
		
		return true;
	}

	@Override
	public void bind(final Request<Delor> request, final Delor entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		
		
		request.bind(entity, errors,"keylet", "subject", "explanation", "startPeriod", "finishPeriod", "income", "moreInfo");
		
		Model model;
		Artifact selectedArtifact;

		model = request.getModel();
		selectedArtifact = this.repository.findArtifactById(Integer.parseInt(model.getString("artifacts")));
		entity.setArtifact(selectedArtifact);
		

	}

	@Override
	public void unbind(final Request<Delor> request, final Delor entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		List<Artifact> artifacts;
		
		List<Delor> lp=this.repository.findAllDelor();
		Set<Artifact> la= new HashSet<Artifact>();
		for(Delor p:lp) {
		
			la.add(p.getArtifact());
			
		}
		
		artifacts=this.repository.findAllArtifact();	
	
		request.unbind(entity, model,"keylet", "subject", "explanation", "startPeriod", "finishPeriod", "income", "moreInfo");
		
		
		model.setAttribute("isNew", true);
		model.setAttribute("artifacts", artifacts.stream().filter(t->t.getType().equals(ArtifactType.INGREDIENT)).filter(x->!x.isPublished()).filter(y->!la.contains(y)).collect(Collectors.toList()));
	}

	@Override
	public Delor instantiate(final Request<Delor> request) {
		assert request != null;
		
		Delor result;
		
		
		
		result = new Delor();
//		LocalDate localDate = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
//		String formattedString = localDate.format(formatter);
//		String[] ss=formattedString.split("/");
//		result.setCode(ss[0]+"XXXX"+"/"+ss[1]+"/"+ss[2]);
		result.setInstantiationMoment(Calendar.getInstance().getTime());
		
		return result;
	}

	@Override
	public void validate(final Request<Delor> request, final Delor entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		Calendar d=Calendar.getInstance();
		d.setTime(entity.getInstantiationMoment());
		d.add(Calendar.MONTH, 1);
		
		
		if (!errors.hasErrors("startPeriod")) {


			errors.state(request, entity.getStartPeriod().after(d.getTime()), "startPeriod",
					"chef.delor.error.month.startPeriod");
		}
		
		Calendar ds=Calendar.getInstance();
		if(entity.getStartPeriod()!=null ) {
		ds.setTime(entity.getStartPeriod());
		}
		ds.add(Calendar.DAY_OF_YEAR, 7);
		
		
		if (!errors.hasErrors("finishPeriod")) {


			errors.state(request, entity.getFinishPeriod().after(ds.getTime()), "finishPeriod",
					"chef.delor.error.week.finishPeriod");
		}
		
		List<Delor> ls=this.repository.findAllDelor();
		Set<String> ss=new HashSet<String>();
		for(Delor delor:ls) {
			ss.add(delor.getKeylet());
		}
		
		
		if(!errors.hasErrors("keylet")) {
            final Date im = entity.getInstantiationMoment();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(im);
            
            
            final String[] fecha = entity.getKeylet().split(":");
            final Integer anyo = Integer.parseInt(fecha[1].substring(0, 2));
            final Integer mes = Integer.parseInt(fecha[1].substring(2, 4));
            final Integer dia = Integer.parseInt(fecha[1].substring(4, 6));
            
            final String year = String.valueOf(calendar.get(Calendar.YEAR));
            final char[] digitsYear = year.toCharArray();
            final String ten = digitsYear[2] + "";
            final String one = digitsYear[3] +"";
            final String yearTwoDigits = ten + one;
            
            final Integer month = calendar.get(Calendar.MONTH) + 1;
            final Integer day = calendar.get(Calendar.DAY_OF_MONTH);

            final Boolean result = (dia.equals(day)) && (mes.equals(month)) && (anyo.equals(Integer.parseInt(yearTwoDigits)));
            
            errors.state(request, result, "keylet", "chef.delor.form.error.code-date");
            
            errors.state(request,!ss.contains(entity.getKeylet()), "keylet",
					"chef.delor.error.duplicatecode");
        }
		

		
		Money money=entity.getIncome();
		final SystemSettings c = this.repository.findConfiguration();
		if (!errors.hasErrors("income")) {


			errors.state(request, money.getAmount()>=0., "income",
					"chef.delor.error.budget");
			
			errors.state(request, c.getAcceptedCurrencies().contains(money.getCurrency()) ,
					  "income", "chef.delor.not-able-currency");
		}
		

		
		
	}

	@Override
	public void create(final Request<Delor> request, final Delor entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.save(entity);
	}
	

}
