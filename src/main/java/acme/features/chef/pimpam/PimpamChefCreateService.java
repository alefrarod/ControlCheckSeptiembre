package acme.features.chef.pimpam;



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
import acme.entities.pimpam.Pimpam;
import acme.entities.systemSetting.SystemSettings;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractCreateService;
import acme.roles.Chef;

@Service
public class PimpamChefCreateService implements AbstractCreateService<Chef, Pimpam>{
	
	@Autowired
	protected PimpamRepository repository;
		
	// AbstractCreateService<Patron, Patronage> interface ---------------------
	
	@Override
	public boolean authorise(final Request<Pimpam> request) {
		assert request != null;
		
		return true;
	}

	@Override
	public void bind(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		
		
		request.bind(entity, errors,"code", "title", "description", "startPeriod", "finishPeriod", "budget", "link");
		
		Model model;
		Artifact selectedArtifact;

		model = request.getModel();
		selectedArtifact = this.repository.findArtifactById(Integer.parseInt(model.getString("artifacts")));
		entity.setArtifact(selectedArtifact);
		

	}

	@Override
	public void unbind(final Request<Pimpam> request, final Pimpam entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		List<Artifact> artifacts;
		
		List<Pimpam> lp=this.repository.findAllPimpam();
		Set<Artifact> la= new HashSet<Artifact>();
		for(Pimpam p:lp) {
			la.add(p.getArtifact());
		}
		
		artifacts=this.repository.findAllArtifact();	
	
		request.unbind(entity, model,"code", "title", "description", "startPeriod", "finishPeriod", "budget", "link");
		
		
		model.setAttribute("isNew", true);
		model.setAttribute("artifacts", artifacts.stream().filter(x->!x.isPublished()).filter(y->!la.contains(y)).collect(Collectors.toList()));
	}

	@Override
	public Pimpam instantiate(final Request<Pimpam> request) {
		assert request != null;
		
		Pimpam result;
		
		
		
		result = new Pimpam();
//		LocalDate localDate = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
//		String formattedString = localDate.format(formatter);
//		String[] ss=formattedString.split("/");
//		result.setCode(ss[0]+"XXXX"+"/"+ss[1]+"/"+ss[2]);
		result.setInstantiationMoment(Calendar.getInstance().getTime());
		
		return result;
	}

	@Override
	public void validate(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		Calendar d=Calendar.getInstance();
		d.setTime(entity.getInstantiationMoment());
		d.add(Calendar.MONTH, 1);
		
		
		if (!errors.hasErrors("startPeriod")) {


			errors.state(request, entity.getStartPeriod().after(d.getTime()), "startPeriod",
					"chef.pimpam.error.month.startPeriod");
		}
		
		Calendar ds=Calendar.getInstance();
		if(entity.getStartPeriod()!=null ) {
		ds.setTime(entity.getStartPeriod());
		}
		ds.add(Calendar.DAY_OF_YEAR, 7);
		
		
		if (!errors.hasErrors("finishPeriod")) {


			errors.state(request, entity.getFinishPeriod().after(ds.getTime()), "finishPeriod",
					"chef.pimpam.error.week.finishPeriod");
		}
		
		
		if(!errors.hasErrors("code")) {
            final Date im = entity.getInstantiationMoment();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(im);
            
            
            final String[] fecha = entity.getCode().split("/");
            final Integer dia = Integer.parseInt(fecha[2]);
            final Integer mes = Integer.parseInt(fecha[1]);
            final Integer anyo = Integer.parseInt(fecha[0].substring(0, 2));
            
            final String year = String.valueOf(calendar.get(Calendar.YEAR));
            final char[] digitsYear = year.toCharArray();
            final String ten = digitsYear[2] + "";
            final String one = digitsYear[3] +"";
            final String yearTwoDigits = ten + one;
            
            final Integer month = calendar.get(Calendar.MONTH) + 1;
            final Integer day = calendar.get(Calendar.DAY_OF_MONTH);

            final Boolean result = (dia.equals(day)) && (mes.equals(month)) && (anyo.equals(Integer.parseInt(yearTwoDigits)));
            
            errors.state(request, result, "code", "chef.pimpam.form.error.code-date");
        }
		

		
		Money money=entity.getBudget();
		final SystemSettings c = this.repository.findConfiguration();
		if (!errors.hasErrors("budget")) {


			errors.state(request, money.getAmount()>=0., "budget",
					"chef.pimpam.error.budget");
			
			errors.state(request, c.getAcceptedCurrencies().contains(money.getCurrency()) ,
					  "budget", "chef.pimpam.not-able-currency");
		}
		

		
		
	}

	@Override
	public void create(final Request<Pimpam> request, final Pimpam entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.save(entity);
	}
	

}
