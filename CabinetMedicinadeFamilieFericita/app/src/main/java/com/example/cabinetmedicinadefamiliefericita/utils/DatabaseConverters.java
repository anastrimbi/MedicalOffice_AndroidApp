package com.example.cabinetmedicinadefamiliefericita.utils;

import androidx.room.TypeConverter;

import com.example.cabinetmedicinadefamiliefericita.enums.Gen;
import com.example.cabinetmedicinadefamiliefericita.enums.TesteAnaliza;
import com.example.cabinetmedicinadefamiliefericita.enums.TipCadreMedicale;
import com.example.cabinetmedicinadefamiliefericita.enums.TipConsultatie;

public class DatabaseConverters {
    @TypeConverter
    public static TipCadreMedicale tipFromString(String value) {
        return value == null ? TipCadreMedicale.Asistent : TipCadreMedicale.valueOf(value);
    }

    @TypeConverter
    public static String tipCadreMedicaleToString(TipCadreMedicale tip) {
        return tip == null ? TipCadreMedicale.Asistent.name() : tip.name();
    }

    @TypeConverter
    public static Gen genFromString(String value) {
        return value == null ? Gen.F : Gen.valueOf(value);
    }

    @TypeConverter
    public static String genToString(Gen gen) {
        return gen == null ? Gen.F.name() : gen.name();
    }

    @TypeConverter
    public static TesteAnaliza testeAnalizaFromString(String value) {
        return value == null ? TesteAnaliza.Sange : TesteAnaliza.valueOf(value);
    }

    @TypeConverter
    public static String genToString(TesteAnaliza testAnaliza) {
        return testAnaliza == null ? TesteAnaliza.Sange.name() : testAnaliza.name();
    }

    @TypeConverter
    public static TipConsultatie tipConsultatieFromString(String value) {
        return value == null ? TipConsultatie.EvaluareInitiala : TipConsultatie.valueOf(value);
    }

    @TypeConverter
    public static String tipConsultatieToString(TipConsultatie tipConsultatie) {
        return tipConsultatie == null ? TipConsultatie.EvaluareInitiala.name() : tipConsultatie.name();
    }
}
