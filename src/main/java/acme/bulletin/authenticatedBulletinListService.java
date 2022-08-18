package acme.features.authenticated.bulletin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.bulletin.Bulletin;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.roles.Authenticated;
import acme.framework.services.AbstractListService;

@Service
public class authenticatedBulletinListService implements AbstractListService<Authenticated, Bulletin> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected authenticatedBulletinRepository repository;

	// AbstractListService<Anonymous, Bulletin>  interface -------------------------


	@Override
	public boolean authorise(final Request<Bulletin> request) {
		assert request != null;

		return true;
	}
	
	@Override
	public Collection<Bulletin> findMany(final Request<Bulletin> request) {
		assert request != null;

		Collection<Bulletin> result;

		result = this.repository.findManyBulletin();

		return result;
	}
	
	@Override
	public void unbind(final Request<Bulletin> request, final Bulletin entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "heading", "text", "link");
	}

}