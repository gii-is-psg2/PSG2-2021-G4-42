package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adopcion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.SolicitudAdopcion;
import org.springframework.samples.petclinic.service.AdopcionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SolicitudAdopcionService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdopcionController {
	private static final String VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM = "adopciones/createOrUpdateAdopcionesForm";
	private static final String VIEWS_ADOPCION_LIST = "adopciones/adopcionList";
	private static final String VIEWS_ADOPCION_INTERESADOS_FORM = "adopciones/interesadosAdopciones";
	private static final String VIEWS_ADOPCION_SOLICITUD_FORM = "adopciones/solicitarAdopcion";

	@Autowired
	private PetService petService;
	@Autowired
	private OwnerService ownerService;
	@Autowired
	private AdopcionService adopcionService;
	@Autowired
	private SolicitudAdopcionService solicitudAdopcionService;

	@GetMapping("/adopciones")
	public String adopciones(final ModelMap model) {
		model.addAttribute("adopciones", this.adopcionService.findAll());
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<Owner> loggedOwner=ownerService.findOwnerByUsername(username);
		if(!loggedOwner.isPresent()) {
			return "welcome";
		}
		model.addAttribute("loggedOwner",loggedOwner.get());
		return "/adopciones/adopcionList";		
	}

	@GetMapping(value="/owners/{ownerId}/adopciones/{petId}/new")
	public String newAdopcion(final ModelMap model,@PathVariable("petId") final int petId,@PathVariable("ownerId") final int ownerId) {
		Pet pet=petService.findPetById(petId);
		Owner o=ownerService.findOwnerById(ownerId);
//		Adopcion a=adopcionService.findAdopcionByIdPetId(pet.getId());
		model.addAttribute("pet", pet);
		model.addAttribute("owner",o);
		model.addAttribute("fecha",LocalDate.now());
		return AdopcionController.VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM;
	}
	@PostMapping(value="/owners/{ownerId}/adopciones/{petId}/new")
	public String newAdopcionPost(final ModelMap model,@PathVariable("petId") final int petId,@PathVariable("ownerId") final int ownerId) {
		Pet pet=petService.findPetById(petId);
		Owner o=ownerService.findOwnerById(ownerId);
		try {
			Adopcion a= new Adopcion();
			a.setFechaPuestaEnAdopcion(LocalDate.now());
			a.setPet(pet);
			adopcionService.save(a);
		}catch (final Exception e) {		
			Adopcion a=adopcionService.findAdopcionByIdPetId(pet.getId());
			model.addAttribute("message", "La mascota ya está en adopción");
			model.addAttribute("messageType", "danger");
			model.addAttribute("pet",pet);
			adopcionService.delete(a);
		//	this.addModelData(model, adopcion);
			return AdopcionController.VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM;
		}
//		model.addAttribute("pet", pet);
//		model.addAttribute("owner", o);
		Adopcion a=adopcionService.findAdopcionByIdPetId(o.getId());
		model.addAttribute("adopciones", a);
		return "redirect:/owners/"+o.getId();
	}
	
	
	@GetMapping(value="/adopciones/delete/{adopcionId}")
	public String deleteAdopcion(@PathVariable final int adopcionId, final ModelMap model) {
		final Optional<Adopcion> adopcion = this.adopcionService.findById(adopcionId);
		
		if(!adopcion.isPresent()) {
			model.addAttribute("message", "La adopcion seleccionada no existe: " + adopcionId);
			model.addAttribute("messageType", "warning");
			return this.adopciones(model);
		}
		
		final Pet pet = adopcion.get().getPet();
		final Owner owner = pet.getOwner();
		
		final String usernameOwner = owner.getUser().getUsername();
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final String rol = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().toString();
		
		if(username.equals(usernameOwner)||rol.equals("admin")) {
			try {
				this.adopcionService.delete(adopcion.get());
			}catch(final Exception e) {
				
			}
		}
		return "redirect:/owners/" + owner.getId();
	}
	@GetMapping(value="/adopciones/solicitud/{adopcionId}")
	public String solicitudAdopcion(@PathVariable final int adopcionId, final ModelMap model) {
		final Optional<Adopcion> adopcion = this.adopcionService.findById(adopcionId);
		if(!adopcion.isPresent()) {
			model.addAttribute("message", "La adopcion seleccionada no existe: " + adopcionId);
			model.addAttribute("messageType", "warning");
			return this.adopciones(model);
		}		
		final Pet pet = adopcion.get().getPet();
		final Owner owner = pet.getOwner();			
		SolicitudAdopcion s= new SolicitudAdopcion();
		s.setNuevoOwner(owner);
		s.setFechaSolicitud(LocalDate.now());
		model.addAttribute("solicitud",s);
		model.addAttribute("pet", pet);
		model.addAttribute("adopcion", adopcion.get());
		model.addAttribute("owner", owner.getId());

		return AdopcionController.VIEWS_ADOPCION_SOLICITUD_FORM;
	}
	@PostMapping(value="/adopciones/solicitud/{adopcionId}")
	public String solicitudAdopcionPost(@PathVariable final int adopcionId,@Valid SolicitudAdopcion solicitud, final BindingResult result, final ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("message", "No se ha podido completar la solicitud de adopción. ");
			model.addAttribute("messageType", "warning");
			return solicitudAdopcion(adopcionId, model);
		}
		solicitud.setFechaSolicitud(LocalDate.now());
		model.addAttribute(solicitud);
		solicitudAdopcionService.saveSolicitud(solicitud);
		model.addAttribute("message", "La solicitud se ha creado correctamente ");
	//	model.addAttribute("messageType", "info");
		return adopciones(model);
	}
	
	@GetMapping(value="/adopciones/interesados/{petId}")
	public String interesadosAdopcion(@PathVariable final int petId, final ModelMap model) {
		Set<SolicitudAdopcion> s=solicitudAdopcionService.findSolicitudAdopcionByPetId(petId);
		model.addAttribute("solicitudes",s);

		return AdopcionController.VIEWS_ADOPCION_INTERESADOS_FORM;
	}
	
	@GetMapping(value="/adopciones/solicitudAdopcion/{solicitudId}/aceptar")
	public String interesadosAdopcionAceptar(@PathVariable final int solicitudId, final ModelMap model) {
		SolicitudAdopcion s=solicitudAdopcionService.findSolicitudById(solicitudId).get();
		Owner o=s.getAdopcion().getPet().getOwner();
		Owner no=s.getNuevoOwner();
		s.getAdopcion().getPet().setOwner(no);//cambiamos el owner de la mascota
		//solicitudAdopcionService.deleteSolicitud(s);
		try {
			petService.savePet(s.getAdopcion().getPet());
		} catch (DataAccessException | DuplicatedPetNameException e) {
			model.addAttribute("message","No se ha podido realizar la adopcion");
			return "redirect:/owners/" + o.getId();
		}
		Collection<SolicitudAdopcion> c=solicitudAdopcionService.findSolicitudAdopcionByPetId(s.getAdopcion().getPet().getId());//borro todas las solicitudes de adopcion de este pet 
		for(SolicitudAdopcion sa:c) {
			solicitudAdopcionService.deleteSolicitud(sa);
		}
		
		adopcionService.delete(s.getAdopcion());
		model.addAttribute("message", "La solicitud se ha procesado correctamente ");
		
		return "redirect:/owners/" + o.getId();
	}
	
	@GetMapping(value="/adopciones/solicitudAdopcion/{solicitudId}/denegar")
	public String interesadosAdopcionDenegar(@PathVariable final int solicitudId, final ModelMap model) {
		SolicitudAdopcion s=solicitudAdopcionService.findSolicitudById(solicitudId).get();
		Owner o=s.getAdopcion().getPet().getOwner();
		solicitudAdopcionService.deleteSolicitud(s);
		model.addAttribute("message", "La solicitud se ha borrado correctamente ");
		
		return "redirect:/owners/" + o.getId();
	}
	public void addModelData(final ModelMap model, final Adopcion adopcion) {

		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final List<Pet> pets = this.petService.findPetsByOwner(username);		
		model.addAttribute("pets", pets);
		final List<Adopcion> adopciones = (List<Adopcion>) this.adopcionService.findAll();
		model.addAttribute("adopciones", adopciones);
	}
}
