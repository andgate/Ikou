package com.andgate.ikou.shader.bloom;

import com.andgate.ikou.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class BloomShaderLoader {

    private static final String BLOOM_FOLDER = Constants.SHADER_FOLDER + "bloom/";

	static final public ShaderProgram createShader(String vertexName,
			String fragmentName) {

		String vertexShader = Gdx.files.internal(
                BLOOM_FOLDER + vertexName
						+ ".vertex.glsl").readString();
		String fragmentShader = Gdx.files.internal(
                BLOOM_FOLDER + fragmentName
						+ ".fragment.glsl").readString();
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) {
			System.out.println(shader.getLog());
			Gdx.app.exit();
		} else
			Gdx.app.log("shader compiled", shader.getLog());
		return shader;
	}
}
