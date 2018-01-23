package org.lpro.boundary;

import java.net.URI;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.lpro.entity.Categorie;
import org.lpro.entity.Sandwich;
import org.lpro.entity.Tailles;

@Stateless
@Path("sandwichs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SandwichResource
{
    @Inject 
    SandwichManager sm;

    @Inject 
    CategorieManager cm;

    @Context
    UriInfo uriInfo;

    /********************************************************************
     * 
     * Route permettant de récupérer la liste des sandwichs
     * 
     *********************************************************************/

    @GET
    public Response getSandwichs(@QueryParam("type") String ptype, 
                                @QueryParam("img") String img,
                                @DefaultValue("1") @QueryParam("page") int page, 
                                @DefaultValue("10") @QueryParam("size") int size)
    {
        // Si les params sont invalides, on les corrige
        if(page < 1) 
            page = 1;
        if(size < 1) 
            size = 10;

        // Si la page demandé est plus grande que la dernière page, alors on renvoi la dernière page
        int lastPage = (int) ((sm.count() / size) + 1); 
        if(page > lastPage) 
            page = lastPage;

        JsonObject json = Json.createObjectBuilder()
                            .add("type", "collection")
                            .add("count", sm.count())
                            .add("size", size)
                            .add("sandwichs", getSandwichList(ptype, img, page, size))
                            .build();
        
        return Response.ok(json).build();
    }

    private JsonValue getSandwichList(String ptype, String img, int page, int size) 
    {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        this.sm.findAll(ptype, img, page, size).forEach( (s) -> 
        {
            jab.add(buildJson(s));
        });    
        
		return jab.build();
    }

    private JsonValue buildJson(Sandwich s) 
    {
        return Json.createObjectBuilder()
                .add("id", s.getId())
                .add("nom", s.getNom())
                .add("type_pain", s.getType())
                .add("links", buildJsonLink(s.getId()))
                .build();
    }
    
    private JsonValue buildJsonLink(String id) 
    {
        return Json.createObjectBuilder()
                .add("self", Json.createObjectBuilder()
                            .add("href", "/sandwichs/" + id + "/")
                            .build())
                .build();

    }
    
    /*********************************************************************
     * 
     * Route permettant de récupérer les détails d'un sandwich
     * 
     *********************************************************************/
    
    @GET
    @Path("{id}")
    public Response getOneSandwich(@PathParam("id") String id, @Context UriInfo uriInfo) 
    {
        return Optional.ofNullable(sm.findById(id))
                .map(s -> Response.ok(buildJsonSandwich(s)).build())
                .orElseThrow(() -> new SandwichNotFound("Ressource non disponible " + uriInfo.getPath()));
    }

    private Object buildJsonSandwich(Sandwich s) 
    {
        JsonArrayBuilder categs = Json.createArrayBuilder();
        s.getCategorie().forEach( c ->
        {
            categs.add(buildJsonCategs(c));
        });

        JsonArrayBuilder tailles = Json.createArrayBuilder();
        s.getTailles().forEach( t -> 
        {
            tailles.add(buildJsonTailles(t));
        });

        return Json.createObjectBuilder()
            .add("id", s.getId())
            .add("nom", s.getNom())
            .add("desc", s.getDescription())
            .add("type_pain", s.getType())
            .add("img", s.getImg())
            .add("categories", categs.build())
            .add("tailles", tailles.build())
            .add("links", buildHATEOSLinks(s))
            .build();
    }
    
    private JsonValue buildJsonCategs(Categorie c) 
    {
        return Json.createObjectBuilder()
            .add("id", c.getId())
            .add("nom", c.getNom())
            .add("desc", c.getDescription())
            .build();
    }
    
    private JsonValue buildJsonTailles(Tailles t) 
    {
        return Json.createObjectBuilder()
            .add("id", t.getId())
            .add("nom", t.getNom())
            .add("prix", t.getPrix())
            .build();
    }
    
    private JsonValue buildHATEOSLinks(Sandwich s) 
    {
        JsonObjectBuilder categs = Json.createObjectBuilder()
            .add("href", uriInfo.getPath() + "/categories/");

        JsonObjectBuilder tailles = Json.createObjectBuilder()
            .add("href", uriInfo.getPath() + "/tailles/");

        return Json.createObjectBuilder()
            .add("categories", categs.build())
            .add("tailles", tailles.build())
            .build();
	}

    /*********************************************************************
     * 
     * Route permettant de récupérer la liste des catégories d'un sadnwich
     * 
     *********************************************************************/

    @GET
    @Path("{id}/categories")
    public Response getCategoriesBySandwich(@PathParam("id") String id)
    {
        return Optional.ofNullable(sm.findById(id))
            .map(s -> Response.ok(buildCategoryToSandwich(s)).build())
            .orElseThrow( () -> new SandwichNotFound("Ressource non disponible " + uriInfo.getPath()));
    }

    private JsonObject buildCategoryToSandwich(Sandwich s)
    {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        s.getCategorie().forEach( c ->
        {
            jab.add(buildJsonForCategory(c));
        });

        return Json.createObjectBuilder()
            .add("categories", jab.build())
            .build();
    }

    private JsonValue buildJsonForCategory(Categorie c) 
    {
        String uriCategory = uriInfo.getBaseUriBuilder()
            .path(CategorieResource.class)
            .path(c.getId() + "")
            .build()
            .toString();

        JsonObject job = Json.createObjectBuilder()
            .add("href", uriCategory)
            .add("rel", "self")
            .build();
        
        JsonArrayBuilder links = Json.createArrayBuilder();
        links.add(job);

        return Json.createObjectBuilder()
            .add("id", c.getId())
            .add("nom", c.getNom())
            .add("desc", c.getDescription())
            .add("links", links)
            .build();
	}

    /*********************************************************************
     * 
     * Route permettant de récupérer la liste des tailles d'un sadnwich
     * 
     *********************************************************************/

    @GET
    @Path("{id}/tailles")
    public Response getTaillesBySandwich(@PathParam("id") String id)
    {
        return Optional.ofNullable(sm.findById(id))
            .map(s -> Response.ok(buildTailleBySandwich(s)).build())
            .orElseThrow(() -> new SandwichNotFound("Ressource non disponible " + uriInfo.getPath()));
    }

    private JsonObject buildTailleBySandwich(Sandwich s)
    {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        s.getTailles().forEach(t ->
        {
            jab.add(buildJsonForTaille(t));
        });

        return Json.createObjectBuilder()
            .add("tailles", jab.build())
            .build();
    }

    private JsonValue buildJsonForTaille(Tailles t)
    {
        String uriTailles = uriInfo.getBaseUriBuilder()
            .path(TailleResource.class)
            .path(t.getId() + "")
            .build()
            .toString();
        
        JsonObject job = Json.createObjectBuilder()
            .add("href", uriTailles)
            .add("rel", "self")
            .build();

        JsonArrayBuilder links = Json.createArrayBuilder();
        links.add(job);

        return Json.createObjectBuilder()
            .add("id", t.getId())
            .add("nom", t.getNom())
            .add("prix", t.getPrix())
            .add("links", links)
            .build();
    }

    /*********************************************************************
     * 
     * Route permettant de créer un nouveau sandwich
     * 
     *********************************************************************/

	@POST
    public Response newSandwich(@Valid Sandwich s, @Context UriInfo uriInfo)
    {
        Sandwich newOne = sm.save(s);
        String id = newOne.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();

        return Response.created(uri).build();
    }

    /*********************************************************************
     * 
     * Route permettant de supprimer un sandwich
     * 
     *********************************************************************/

    @DELETE
    @Path("{id}")
    public Response suppression(@PathParam("id") long id)
    {
        sm.delete(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /*********************************************************************
     * 
     * Route permettant de modifier un sandwich
     * 
     *********************************************************************/

    @PUT
    @Path("{id}")
    public Sandwich update(@PathParam ("id") String id, Sandwich s)
    {
        s.setId(id);

        return sm.save(s);
    }
}