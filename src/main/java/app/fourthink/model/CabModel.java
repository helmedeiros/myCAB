package app.fourthink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cab_model")
public class CabModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CabCategory category;

    public CabModel() {
    }

    public CabModel(String make, String model, CabCategory category) {
        this.make = make;
        this.model = model;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public CabCategory getCategory() {
        return category;
    }

    public String displayName() {
        return make + " " + model;
    }

    public String fullLabel() {
        return displayName() + " (" + CabKind.label(category) + ")";
    }
}
