package acme.features.epicure.fineDish;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.fineDish.FineDish;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.entities.AbstractEntity;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;
import acme.roles.Epicure;

@Service
public class FineDishEpicureShowService implements AbstractShowService<Epicure, FineDish>{
	
	// Internal state ---------------------------------------------------------

			@Autowired
			protected FineDishEpicureRepository repository;

			// AbstractShowService<Anonymous, Announcement> interface --------------------------

			@Override
			public boolean authorise(final Request<FineDish> request) {
				assert request != null;

				Integer id = request.getModel().getInteger("id");
				Optional<AbstractEntity> result = this.repository.findById(id);
				Principal principal = request.getPrincipal();
				
				return result.isPresent() && ((FineDish)result.get()).getEpicure().getId() == principal.getActiveRoleId();
			}

			@Override
			public void unbind(final Request<FineDish> request, final FineDish entity, final Model model) {
				assert request != null;
				assert entity != null;
				assert model != null;

				request.unbind(entity, model, "status", "code", "request", "budget", "initialDate", "finishDate", "url", "isPublish" ,"chef", "epicure");
				model.setAttribute("chefId", entity.getChef().getUserAccount().getId());
				model.setAttribute("isNew", false);
			}

			@Override
			public FineDish findOne(final Request<FineDish> request) {
				assert request != null;

				FineDish result;
				int id;

				id = request.getModel().getInteger("id");
				result = this.repository.findOneFineDishById(id);

				return result;
			}

}
