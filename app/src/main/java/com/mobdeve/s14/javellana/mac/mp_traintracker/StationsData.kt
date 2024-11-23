package com.mobdeve.s14.javellana.mac.mp_traintracker

import com.google.android.gms.maps.model.LatLng

object StationsData {
    val allStations = listOf(
        // LRT-1 Stations
        Station("Baclaran", LatLng(14.5342, 120.9984)),
        Station("EDSA", LatLng(14.5387, 121.0007)),
        Station("Libertad", LatLng(14.5491, 120.9989)),
        Station("Gil Puyat", LatLng(14.5542, 120.9972)),
        Station("Vito Cruz", LatLng(14.5633, 120.9949)),
        Station("Quirino", LatLng(14.5703, 120.9916)),
        Station("Pedro Gil", LatLng(14.5763, 120.9882)),
        Station("United Nations", LatLng(14.5825, 120.9846)),
        Station("Central Terminal", LatLng(14.5928, 120.9816)),
        Station("Carriedo", LatLng(14.5990, 120.9813)),
        Station("Doroteo Jose", LatLng(14.6055, 120.9820)),
        Station("Bambang", LatLng(14.6112, 120.9825)),
        Station("Tayuman", LatLng(14.6167, 120.9828)),
        Station("Blumentritt", LatLng(14.6227, 120.9835)),
        Station("Abad Santos", LatLng(14.6306, 120.9814)),
        Station("R. Papa", LatLng(14.6362, 120.9823)),
        Station("5th Avenue", LatLng(14.6445, 120.9763)),
        Station("Monumento", LatLng(14.6544, 120.9839)),
        Station("Balintawak", LatLng(14.6575, 121.0041)),
        Station("Roosevelt", LatLng(14.6576, 121.0211)),
        Station("North Ave", LatLng(14.6522, 121.0323)),
        // LRT-2 Stations
        Station("Recto", LatLng(14.6035, 120.9835)),
        Station("Legarda", LatLng(14.6009, 120.9926)),
        Station("Pureza", LatLng(14.6019, 121.0054)),
        Station("V. Mapa", LatLng(14.6041, 121.0171)),
        Station("J. Ruiz", LatLng(14.6106, 121.0262)),
        Station("Gilmore", LatLng(14.6135, 121.0342)),
        Station("Betty Go-Belmonte", LatLng(14.6186, 121.0428)),
        Station("Cubao", LatLng(14.6195, 121.0511)),
        Station("Anonas", LatLng(14.6280, 121.0648)),
        Station("Katipunan", LatLng(14.6308, 121.0727)),
        Station("Santolan", LatLng(14.6221, 121.0859)),
        Station("Marikina-Pasig", LatLng(14.6204, 121.1003)),
        Station("Antipolo", LatLng(14.62472, 121.12111)),

        // MRT-3 Stations
        Station("North Avenue", LatLng(14.6543, 121.0355)),
        Station("Quezon Avenue", LatLng(14.6429, 121.0374)),
        Station("GMA Kamuning", LatLng(14.6349, 121.0412)),
        Station("Araneta Center-Cubao", LatLng(14.6191, 121.0512)),
        Station("Santolan-Annapolis", LatLng(14.6086, 121.0557)),
        Station("Ortigas", LatLng(14.5883, 121.0574)),
        Station("Shaw Boulevard", LatLng(14.5814, 121.0533)),
        Station("Boni", LatLng(14.5742, 121.0484)),
        Station("Guadalupe", LatLng(14.5671, 121.0432)),
        Station("Buendia", LatLng(14.5539, 121.0334)),
        Station("Ayala", LatLng(14.5495, 121.0280)),
        Station("Magallanes", LatLng(14.5419, 121.0197)),
        Station("Taft Avenue", LatLng(14.5378, 121.0014))
    )

    val lrt1Stations = allStations.subList(0, 21)
    val lrt2Stations = allStations.subList(21, 34)
    val mrt3Stations = allStations.subList(34, allStations.size)
}
