package app.fourthink.model;

import com.vercer.engine.persist.annotation.Key;

public class City {
	@Key
	private Long id;
	private String cityName;				//Nome da cidade. Ex.: Porto Alegre
	private String state;					//Nome do estado. Ex.: Rio Grande do Sul
	private String stateAcronym;			//Sigla do estado. Ex.: RS;
	private double startValue;				//Valor da bandeirada em táxi comum. Ex.: 1.89
	private double startValueSUV;			//Valor bandeirada de veículos-táxis do tipo SUV/Perua. ex.: 2.46 
	private double commonTax;				//Valor do Km rodado na bandeira 1
	private double specialTax;				//Valor do Km rodado na bandeira 2
	private double commonTaxSUV; 			//Valor do Km rodado na bandeira 1 em táxi especial/SUV
	private double specialTaxSUV; 			//Valor do Km rodado na bandeira 2 em táxi especial/SUV
	private int crossUrbanBoundaryTax;		//Valor de taxa adicional 
	
	
	
	
	public City(String cityName, String state, String stateAcronym) {
		super();
		this.cityName = cityName;
		this.state = state;
		this.stateAcronym = stateAcronym;
	}
	
	public Long getId(){
		return this.id;
	}
	public void setId(Long id){
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateAcronym() {
		return stateAcronym;
	}
	public void setStateAcronym(String stateAcronym) {
		this.stateAcronym = stateAcronym;
	}
	public double getStartValue() {
		return startValue;
	}
	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}
	public double getStartValueSUV() {
		return startValueSUV;
	}
	public void setStartValueSUV(double startValueSUV) {
		this.startValueSUV = startValueSUV;
	}
	public double getCommonTax() {
		return commonTax;
	}
	public void setCommonTax(double commonTax) {
		this.commonTax = commonTax;
	}
	public double getSpecialTax() {
		return specialTax;
	}
	public void setSpecialTax(double specialTax) {
		this.specialTax = specialTax;
	}
	public double getCommonTaxSUV() {
		return commonTaxSUV;
	}
	public void setCommonTaxSUV(double commonTaxSUV) {
		this.commonTaxSUV = commonTaxSUV;
	}
	public double getSpecialTaxSUV() {
		return specialTaxSUV;
	}
	public void setSpecialTaxSUV(double specialTaxSUV) {
		this.specialTaxSUV = specialTaxSUV;
	}
	public int getCrossUrbanBoundaryTax() {
		return crossUrbanBoundaryTax;
	}
	public void setCrossUrbanBoundaryTax(int crossUrbanBoundaryTax) {
		this.crossUrbanBoundaryTax = crossUrbanBoundaryTax;
	}

}
