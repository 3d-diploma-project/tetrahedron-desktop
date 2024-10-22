package com.devengine.core.entity;

import com.devengine.core.utils.Consts;
import org.joml.Vector4f;

public class Material {

    private Vector4f ambientColour, diffuseColour, specularColour;
    private float reflectance;
    private Texture texture;

    public Material() {
        this.ambientColour = Consts.DEFAULT_COLOUR;
        this.diffuseColour = Consts.DEFAULT_COLOUR;
        this.specularColour = Consts.DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance) {
        this(colour, colour, colour, reflectance, null);
    }

    public Material(Vector4f colour, float reflectance, Texture texture) {
        this(colour, colour, colour, reflectance, texture);
    }

    public Material(Texture texture) {
        this(Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, 0, texture);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, float reflectance, Texture texture) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.reflectance = reflectance;
        this.texture = texture;
    }

    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    public Vector4f getSpecularColour() {
        return specularColour;
    }

    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }

}
