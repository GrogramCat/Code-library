protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	int w = customMeasure(widthMeasureSpec);
	int h = customMeasure(heightMeasureSpec);
	setMeasuredDimension(w, h);
}

private int customMeasure(int measureSpec) {
	int result = 0;
	int specMode = MeasureSpec.getMode(measureSpec);
	int specSize = MeasureSpec.getSize(measureSpec);
	if (specMode == MeasureSpec.EXACTLY) {
		result = specSize;
	} else {
		result = 200;
		if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
	}
	return result;
}