/**
 * Created by Jason on 11/01/2018.
 */

package com.bell_inter.eggster;

import android.util.Log;

import static java.lang.Math.PI;
import static java.lang.Math.log;
import static java.lang.Math.pow;

public class CEgg
{
    // reference...
    // egg cooking :-
    // https://newton.ex.ac.uk/teaching/CDHW/egg/
    // https://newton.ex.ac.uk/teaching/CDHW/egg/CW061201-1.pdf

    // boiling point at pressure :-
    // this is derived from the Clausius - Claperon Relation
    // (which can be used to relate boiling points of liquids to ambient barometric conditions)
    // https://en.wikipedia.org/wiki/Clausius%E2%80%93Clapeyron_relation



    // properties...

    // constants...
    private static final String TAG = new String("CEgg");

    // physical constants...
    private static final double EGG_SPECIFIC_HEAT_CAPACITY = 3.7; //  J g^−1 K^-1
    private static final double EGG_THERMAL_CONDUCTIVITY = - 5.4E10-3;  //  W cm^−1 K^−1
    private static final double EGG_DENSITY = 1.038;  // g cm^-1
    private static final double BOILING_POINT_WATER = (99.97 + 273.15);  // boiling point of water (Kelvin)
    private static final double STANDARD_PRESSURE = 1013.25;  // STANDARD PRESSURE (milliBar)
    private static final double UNIVERSAL_GAS_CONSTANT = 0.008314;  // KJ Mol^-1 K^-1
    private static final double LATENT_HEAT_OF_VAPOURIZATION = 273.15;  // KJ Mol^-1
    private static final double KELVIN_OFFSET = 273.15;
    private static final double DEFAULT_VALUE = -9999;

    // egg constants...
    private static final double  SIZE0_MASS = 77.5; // g
    private static final double  SIZE1_MASS = 72.5; // g
    private static final double  SIZE2_MASS = 67.5; // g
    private static final double  SIZE3_MASS = 62.5; // g
    private static final double  SIZE4_MASS = 57.5; // g
    private static final double  SIZE5_MASS = 52.5; // g
    private static final double  SIZE6_MASS = 47.5; // g
    private static final double  SIZE7_MASS = 42.5; // g
    private static final double  SIZE_0 = 0;
    private static final double  SIZE_1 = 1;
    private static final double  SIZE_2 = 2;
    private static final double  SIZE_3 = 3;
    private static final double  SIZE_4 = 4;
    private static final double  SIZE_5 = 5;
    private static final double  SIZE_6 = 6;
    private static final double  SIZE_7 = 7;

    // user defined / default values...
    // There are fundamentally three user inputs required:-
    // [starting] egg temperature; egg size/mass; final yolk state/temperature
    private double mTegg = DEFAULT_VALUE;   // initial temperature of the egg
    private double mMegg = DEFAULT_VALUE;   // mass of the egg - set by user
    private double mTyolk = DEFAULT_VALUE;  // target final temperature of the yolk; viz the 'cook point' - set by user
    private double mTwater = DEFAULT_VALUE; // temperature of the cooking water (assumed to be boiling
                                            // - temperature calculated fromm ambient pressure.)
    private double mBoilingPoint = BOILING_POINT_WATER;

    // defined values...
    private double mSHCegg = EGG_SPECIFIC_HEAT_CAPACITY; // specific heat capacity of the egg (defined)
    private double mRHOegg = EGG_DENSITY; // density of the egg (defined)
    private double mKegg = EGG_THERMAL_CONDUCTIVITY;   // thermal conductivity of the egg (defined)

    // methods...

    // START OF CONSTRUCTORS
    //
    // there are four constructors...
    //

    public CEgg(double anEggMass, double anEggTemperature, double aYolkTemperature, double anAirPressure)   // parameterised constructor...
    {                                                                                 // Egg mass
        // assume water is boiling...
        // we'll calculate the temperature from ambient pressure...

        SetBoilingPoint4Pressure(anAirPressure);

        mMegg = anEggMass;
        mTegg = anEggTemperature;
        mTyolk = aYolkTemperature;

    }

    public CEgg(int anEggSize, double anEggTemperature, double aYolkTemperature, double anAirPressure)   // parameterised constructor...
    {                                                                              // Egg size (standard sizes)
        // assume water is boiling...
        // we'll calculate the temperature from ambient pressure...

        double eggMass = Size2Mass(anEggSize);
        SetBoilingPoint4Pressure(anAirPressure);

        mMegg = eggMass;
        mTegg = anEggTemperature;
        mTyolk = aYolkTemperature;

    }

    public CEgg(double anEggMass, double anEggTemperature, int aYolkIndex, double anAirPressure)   // parameterised constructor...
    {                                                                                 // Egg mass
        // assume water is boiling...
        // we'll calculate the temperature from ambient pressure...

        double yolkTemperature = YolkState2YolkTemperature(aYolkIndex);
        SetBoilingPoint4Pressure(anAirPressure);

        mMegg = anEggMass;
        mTegg = anEggTemperature;
        mTyolk = yolkTemperature;

    }

    public CEgg(int anEggSize, double anEggTemperature, int aYolkIndex, double anAirPressure)   // parameterised constructor...
    {                                                                              // Egg size (standard sizes)
        // assume water is boiling...
        // we'll calculate the temperature from ambient pressure...

        double eggMass = Size2Mass(anEggSize);
        double yolkTemperature = YolkState2YolkTemperature(aYolkIndex);
        SetBoilingPoint4Pressure(anAirPressure);

        mMegg = eggMass;
        mTegg = anEggTemperature;
        mTyolk = yolkTemperature;

    }

    // END OF CONSTRUCTORS

    public double GetCookTime()
    {
        mTwater = mBoilingPoint;

        Log.d(TAG, "->GetCookTime()");

        Log.d(TAG, "mMegg = " + mMegg);
        Log.d(TAG, "mSHCegg = " + mSHCegg);
        Log.d(TAG, "mRHOegg = " + mRHOegg);
        Log.d(TAG, "mKegg = " + mKegg);
        Log.d(TAG, "mTegg = " + mTegg);
        Log.d(TAG, "mTwater = " + mTwater);
        Log.d(TAG, "mTyolk = " + mTyolk);

        double cookTime  = DEFAULT_VALUE;

        if(mMegg == DEFAULT_VALUE
                || mSHCegg == DEFAULT_VALUE
                || mRHOegg == DEFAULT_VALUE
                || mKegg == DEFAULT_VALUE
                || mTegg == DEFAULT_VALUE
                || mTwater == DEFAULT_VALUE
                || mTyolk == DEFAULT_VALUE)
        {
            Log.d(TAG, "Default values present, returning...");
            return DEFAULT_VALUE;

        }
        else
        {
            cookTime = ((pow(mMegg, 2 / 3) * mSHCegg * pow(mRHOegg, 1 / 3)) / (mKegg * pow(PI, 2) * pow(4 / 3 * PI, 2 / 3))

                    * log(0.76 * ((mTegg - mTwater) / (mTyolk - mTwater))));

            Log.d(TAG, "Calculated cooktime = " + cookTime);
            return cookTime;

        }

    }

    // Setters...
    public double SetmTegg(double mTegg)
    {
        double oldmTegg = this.mTegg = mTegg;
        this.mTegg = mTegg;

        return oldmTegg;

    }

    public double SetmTwater(double mTwater)
    {
        double oldmTwater = this.mTwater = mTwater;
        this.mTwater = mTwater;

        return oldmTwater;

    }

    public double SetmTyolk(double mTyolk)
    {
        double oldmTyolk = this.mTyolk;
        this.mTyolk = mTyolk;

        return oldmTyolk;

    }

    public double SetmMegg(double mMegg)
    {
        double oldmMegg = this.mMegg;
        this.mMegg = mMegg;

        return oldmMegg;

    }

    public double SetmSHCegg(double mSHCegg)
    {
        double oldmSHCegg = this.mSHCegg;
        this.mSHCegg = mSHCegg;

        return oldmSHCegg;

    }

    public double SetmRHOegg(double mRHOegg)
    {
        double oldmRHOegg = this.mRHOegg;
        this.mRHOegg = mRHOegg;

        return oldmRHOegg;

    }

    public double SetmKegg(double mKegg)
    {
        double oldmKegg = this.mKegg;
        this.mKegg = mKegg;

        return oldmKegg;

    }

    // Getters...
    public double GetmTegg()
    {
        return mTegg;

    }

    public double GetmTwater()
    {
        return mTwater;

    }

    public double GetmTyolk()
    {
        return mTyolk;

    }

    public double GetmMegg()
    {
        return mMegg;

    }

    public double GetmSHCegg()
    {
        return mSHCegg;

    }

    public double GetmRHOegg()
    {
        return mRHOegg;

    }

    public double GetmKegg()
    {
        return mKegg;

    }

    public double Size2Mass(int aSize)
    {
        switch(aSize)
        {
            case 0:
                return SIZE0_MASS;

            case 1:
                return SIZE1_MASS;

            case 2:
                return SIZE2_MASS;

            case 3:
                return SIZE3_MASS;

            case 4:
                return SIZE4_MASS;

            case 5:
                return SIZE5_MASS;

            case 6:
                return SIZE6_MASS;

            case 7:
                return SIZE7_MASS;

            default:
                return DEFAULT_VALUE;

        }

    }

    public double Mass2Size(double aMass)
    {
        if(aMass < SIZE7_MASS)
        {
            return SIZE_7;

        }
        else if(aMass < SIZE6_MASS)
        {
            return SIZE_6;

        }
        else if(aMass < SIZE5_MASS)
        {
            return SIZE_5;

        }
        else if(aMass < SIZE4_MASS)
        {
            return SIZE_4;

        }
        else if(aMass < SIZE3_MASS)
        {
            return SIZE_3;

        }
        else if(aMass < SIZE2_MASS)
        {
            return SIZE_2;

        }
        else if(aMass < SIZE1_MASS)
        {
            return SIZE_1;

        }
        else if(aMass < SIZE0_MASS)
        {
            return SIZE_0;

        }
        else
        {
            return DEFAULT_VALUE;

        }

    }

    public double SetBoilingPoint4Pressure(double aPressure)
    {
        // this is derived from the Clausius - Claperon Relation
        // (which can be used to relate boiling points of liquids to ambient barometric conditions)
        // https://en.wikipedia.org/wiki/Clausius%E2%80%93Clapeyron_relation

        double oldBoilingPoint = mBoilingPoint;

        // function elements...
        double lnPressureQuotient = log(aPressure / STANDARD_PRESSURE);
        double constantR = LATENT_HEAT_OF_VAPOURIZATION / UNIVERSAL_GAS_CONSTANT;
        double referenceTemperature = BOILING_POINT_WATER + KELVIN_OFFSET;

        double newBoilingPoint = pow(lnPressureQuotient / constantR - pow(referenceTemperature, -1), -1);

        if(aPressure > 0)
        {
            mBoilingPoint = newBoilingPoint;

        }
        else
        {
            mBoilingPoint = BOILING_POINT_WATER;

        }

        return oldBoilingPoint;

    }

    public double GetBoilingPoint()
    {
        return mBoilingPoint;

    }

    public double YolkState2YolkTemperature(int aYolkState) // TO DO!!
    {
        return 65.0;

    }

    public String YolkTemperature2YolkState(int aYolkState) // TO DO!!
    {
        return "Runny";

    }

}
