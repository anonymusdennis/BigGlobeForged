package builderb0y.bigglobe.rendering;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import builderb0y.autocodec.util.AutoCodecUtil;

@OnlyIn(Dist.CLIENT)
public class ScreenTriangleShader extends Shader {

	public ScreenTriangleShader() {
		try {
			this.compileStage(
				this.vertexStage,
				//language=glsl
				"""
				#version 150
				
				out vec2 texcoord;
				
				void main() {
					texcoord = vec2((gl_VertexID & 1) << 1, gl_VertexID & 2);
					gl_Position = vec4(texcoord * 2.0 - 1.0, 0.0, 1.0);
				}
				"""
			);
		}
		catch (Throwable throwable) {
			this.close();
			throw AutoCodecUtil.rethrow(throwable);
		}
	}
}