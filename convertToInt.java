public final static int convertToInt(Object value, int defaultValue) {
	if(value == null || "".equals(value.toString().trim())) {
		return defaultValue;
	}
	try{
		return Integer.valueOf(value.toString());
	}catch(Exception e) {
		try{
			return Double.valueOf(value.toString()).intValue();
		}catch(Exception e) {
			return defaultValue;
		}
	}
}