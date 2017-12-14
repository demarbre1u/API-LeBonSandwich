package org.lpro.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="Categorie.findAll",query="SELECT c FROM Categorie c")
public class Categorie implements Serializable
{
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String nom;
    @NotNull
    private String description;
    @ManyToMany
    @JoinTable(name="categorie_sandwich")
    private Set<Sandwich> sandwich = new HashSet<Sandwich>();

    public Categorie() {}

    public Categorie(long id, String nom, String description) 
    {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.sandwich = new HashSet<>();
    }

    public long getId() 
    {
        return id;
    }

    public void setId(long id) 
    {
        this.id = id;
    }

    public String getNom() 
    {
        return nom;
    }

    public void setNom(String nom) 
    {
        this.nom = nom;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public Set<Sandwich> getSandwich() 
    {
        return sandwich;
    }

    public void setSandwich(Set<Sandwich> sandwich) 
    {
        this.sandwich = sandwich;
    }
}
