package builderb0y.bigglobe.rendering;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OutOfVramException extends RuntimeException {

	public OutOfVramException() {}

	public OutOfVramException(String message) {
		super(message);
	}

	public OutOfVramException(Throwable cause) {
		super(cause);
	}

	public OutOfVramException(String message, Throwable cause) {
		super(message, cause);
	}
}