package biosim.server.simulation.food;

import biosim.idl.simulation.food.*;
/**
 * Legume
 * @author    Scott Bell
 */

public abstract class Legume extends Planophile{
	protected abstract float getBCF();
	protected abstract float getPhotoperiod();
	protected abstract float getN();
	protected abstract float getTimeAtCanopySenescence();
	protected abstract float getCQYMin();
	protected abstract float getTimeAtCropMaturity();
	protected abstract float getOPF();
	protected abstract float getFreshFactor();
	protected abstract float getFractionOfEdibleBiomass();
	protected abstract float getEdibleFreshBasisWaterContent();
	protected abstract float getInedibleFreshBasisWaterContent();
	protected abstract float getCUEMax();
	protected abstract float getCUEMin();
	public abstract float getPPFNeeded();
	public abstract PlantType getPlantType();
	
	public Legume(ShelfImpl pShelfImpl){
		super(pShelfImpl);
	}

	protected float getCarbonUseEfficiency24(){
		float CUEMax = getCUEMax();
		float timeTillCanopySenescence = getTimeAtCanopySenescence();
		System.out.println("Legume: CUEMax: "+CUEMax);
		System.out.println("Legume: timeTillCanopySenescence: "+timeTillCanopySenescence);
		if (getDaysOfGrowth() < getTimeAtCanopySenescence()){
			return CUEMax; 
		}
		else{
			float CUEMin = getCUEMin();
			float daysOfGrowth = getDaysOfGrowth();
			float timeTillCropMaturity = getTimeAtCropMaturity();
			float calculatedCUE24 = CUEMax - ((CUEMax - CUEMin) * ((daysOfGrowth - timeTillCanopySenescence)) / (timeTillCropMaturity - timeTillCanopySenescence));
			System.out.println("Legume: CUEMin: "+CUEMin);
			System.out.println("Legume: daysOfGrowth: "+daysOfGrowth);
			System.out.println("Legume: timeTillCropMaturity: "+timeTillCropMaturity);
			if (calculatedCUE24 < 0f)
				return 0f;
			else
				return calculatedCUE24;
		}
	}

}
