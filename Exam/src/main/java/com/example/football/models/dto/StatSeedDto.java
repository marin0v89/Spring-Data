package com.example.football.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatSeedDto {
    @XmlElement
    private double passing;
    @XmlElement
    private double shooting;
    @XmlElement
    private double endurance;

    @Positive
    @Min(1)
    public double getPassing() {
        return passing;
    }

    public void setPassing(double passing) {
        this.passing = passing;
    }

    @Positive
    @Min(1)
    public double getShooting() {
        return shooting;
    }

    public void setShooting(double shooting) {
        this.shooting = shooting;
    }

    @Positive
    @Min(1)
    public double getEndurance() {
        return endurance;
    }

    public void setEndurance(double endurance) {
        this.endurance = endurance;
    }
}
