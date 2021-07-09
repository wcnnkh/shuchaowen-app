package scw.app.editable.form;

public class ImageInput extends Input {
	private static final long serialVersionUID = 1L;
	private boolean multiple;

	public ImageInput() {
		super(InputType.IMAGE);
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
}
