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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdopcionController {
	public static final String VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM = "adopciones/createOrUpdateAdopcionesForm";
	public static final String VIEWS_ADOPCION_LIST = "/adopciones/adopcionList";
	public static final String VIEWS_ADOPCION_INTERESADOS_FORM = "adopciones/interesadosAdopciones";
	public static final String VIEWS_ADOPCION_SOLICITUD_FORM = "adopciones/solicitarAdopcion";

	@Autowired
	private PetService petService;
	@Autowired
	private OwnerService ownerService;
	@Autowired
	private AdopcionService adopcionService;
	@Autowired
	private SolicitudAdopcionService solicitudAdopcionService;

	@InitBinder("adopcion")
	public void initAdopcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new AdopcionValidator());
	}
	
	@InitBinder("solicitudAdopcion")
	public void initSolicitudAdopcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new SolicitudAdopcionValidator());
	}
	
	@GetMapping("/adopciones")
	public String adopciones(final ModelMap model) {
		model.addAttribute("adopciones", this.adopcionService.findAll());
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final Optional<Owner> loggedOwner=this.ownerService.findOwnerByUsername(username);
		if(!loggedOwner.isPresent()) {
			return "welcome";
		}
		model.addAttribute("loggedOwner",loggedOwner.get());
		return VIEWS_ADOPCION_LIST;		
	}

	@GetMapping(value="/owners/{ownerId}/adopciones/{petId}/new")
	public String newAdopcion(final ModelMap model,@PathVariable("petId") final int petId,@PathVariable("ownerId") final int ownerId) {
		final Pet pet=this.petService.findPetById(petId);
		final Owner o=this.ownerService.findOwnerById(ownerId);
		
		model.addAttribute("pet", pet);
		model.addAttribute("owner",o);
		model.addAttribute("fecha",LocalDate.now());
		return AdopcionController.VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM;
	}
	@PostMapping(value="/owners/{ownerId}/adopciones/{petId}/new")
	public String newAdopcionPost(final ModelMap model,@PathVariable("petId") final int petId,@PathVariable("ownerId") final int ownerId) {
		final Pet pet=this.petService.findPetById(petId);
		final Owner o=this.ownerService.findOwnerById(ownerId);
		try {
			final Adopcion a= new Adopcion();
			a.setFechaPuestaEnAdopcion(LocalDate.now());
			a.setPet(pet);
			this.adopcionService.save(a);
		}catch (final Exception e) {		
			final Adopcion a=this.adopcionService.findAdopcionByIdPetId(pet.getId());
			model.addAttribute("message", "La mascota ya está en adopción");
			model.addAttribute("messageType", "danger");
			model.addAttribute("pet",pet);
			this.adopcionService.delete(a);
			return AdopcionController.VIEWS_ADOPCION_CREATE_OR_UPDATE_FORM;
		}
		final Adopcion a=this.adopcionService.findAdopcionByIdPetId(o.getId());
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
		final SolicitudAdopcion s= new SolicitudAdopcion();
		
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		final Optional<Owner> loggedOwner=this.ownerService.findOwnerByUsername(username);
		if(!loggedOwner.isPresent()) {
			return "welcome";
		}
		
		s.setNuevoOwner(loggedOwner.get());
		s.setFechaSolicitud(LocalDate.now());
		model.addAttribute("solicitud",s);
		model.addAttribute("pet", pet);
		model.addAttribute("adopcion", adopcion.get());

		return AdopcionController.VIEWS_ADOPCION_SOLICITUD_FORM;
	}
	@PostMapping(value="/adopciones/solicitud/{adopcionId}")
	public String solicitudAdopcionPost(@PathVariable final int adopcionId,@Valid final SolicitudAdopcion solicitud, final BindingResult result, final ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("message", "No se ha podido completar la solicitud de adopción. ");
			model.addAttribute("messageType", "warning");
			return this.solicitudAdopcion(adopcionId, model);
		}
		solicitud.setFechaSolicitud(LocalDate.now());
		model.addAttribute(solicitud);
		this.solicitudAdopcionService.saveSolicitud(solicitud);
		model.addAttribute("message", "La solicitud se ha creado correctamente ");
		return this.adopciones(model);
	}
	
	@GetMapping(value="/adopciones/interesados/{petId}")
	public String interesadosAdopcion(@PathVariable final int petId, final ModelMap model) {
		final Set<SolicitudAdopcion> s=this.solicitudAdopcionService.findSolicitudAdopcionByPetId(petId);
		model.addAttribute("solicitudes",s);

		return AdopcionController.VIEWS_ADOPCION_INTERESADOS_FORM;
	}
	
	@GetMapping(value="/adopciones/solicitudAdopcion/{solicitudId}/aceptar")
	public String interesadosAdopcionAceptar(@PathVariable final int solicitudId, final ModelMap model) {
		final Optional<SolicitudAdopcion> solicitud = this.solicitudAdopcionService.findSolicitudById(solicitudId);
		if(solicitud.isEmpty()) {
			return "welcome";
		}
		final SolicitudAdopcion s = solicitud.get();
		
		final Owner o = s.getAdopcion().getPet().getOwner();
		final Owner no = s.getNuevoOwner();
		s.getAdopcion().getPet().setOwner(no);//cambiamos el owner de la mascota
		
		try {
			this.petService.savePet(s.getAdopcion().getPet());
		} catch (DataAccessException | DuplicatedPetNameException e) {
			model.addAttribute("message","No se ha podido realizar la adopcion");
			return "redirect:/owners/" + o.getId();
		}
		final Collection<SolicitudAdopcion> c=this.solicitudAdopcionService.findSolicitudAdopcionByPetId(s.getAdopcion().getPet().getId());//borro todas las solicitudes de adopcion de este pet 
		for(final SolicitudAdopcion sa:c) {
			this.solicitudAdopcionService.deleteSolicitud(sa);
		}
		
		this.adopcionService.delete(s.getAdopcion());
		model.addAttribute("message", "La solicitud se ha procesado correctamente ");
		
		return "redirect:/owners/" + o.getId();
	}
	
	@GetMapping(value="/adopciones/solicitudAdopcion/{solicitudId}/denegar")
	public String interesadosAdopcionDenegar(@PathVariable final int solicitudId, final ModelMap model) {
		final SolicitudAdopcion s=this.solicitudAdopcionService.findSolicitudById(solicitudId).get();
		final Owner o=s.getAdopcion().getPet().getOwner();
		this.solicitudAdopcionService.deleteSolicitud(s);
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
