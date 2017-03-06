package com.daferto.common;

/**
 * Created by Jesús Tomás on 04/03/2017.
 */

public class CarGrammar extends Grammar{

/*
      hood                  grid 1|2|3..8       completed
      roof                  <nada>              incompleted
      box                                       add sag
      tailgate                                  add mottle
      left door
      right door
      both door
      left fender
      right fender
      both fender
      alfa pillar
      bravo pillar
      charlie pillar

      capó                  grid 1|2|3..8       completo
      techo                 <nada>              defecto
      box                                       añade bollo
      portón box                                añade mancha
      ambas aletas
      ambas puertas
      puerta derecha
      puerta izquierda
      aleta derecha
      aleta izquierda
      ambas puertas
      ambas aletas
      pilar a
      pilar b
      pilar c
      */

    public CarGrammar(String lang){
        super(100); //100 = tamaño máximo de vocabulario
        if(lang.equals("en-US")) {
            newRule("<INI>","<PIECE>","hood","roof","box","tailgate");
            newRule("<INI>","<LEFT_RIGHT>","left","right","both");
            newRule("<LEFT_RIGHT>","<PIECE>","door", "fender");
            newRule("<INI>","<PILLAR>","alfa","bravo","charlie");
            newRule("<PILLAR>","<PIECE>","pillar");

            newRule("<PIECE>","<GRID>","grid");
            newRule("<GRID>", "<NUM>", "one", "two", "three", "four", "five", "six", "seven", "eight");
            newRule("<PIECE>","<NUM>","");

            newRule("<NUM>","<END>","completed","incompleted");
            newRule("<NUM>","<ADD>","add");
            newRule("<ADD>","<END>","sag","mottle");
        } else {
            newRule("<INI>","<PIEZA>","capó","techo","box");
            newRule("<INI>","<PIEZA>","capó","techo","box");
            newRule("<INI>","<PUERTA_ALETA>","puerta","aleta");
            newRule("<PUERTA_ALETA>","<PIEZA>","derecha","izquierda");
            newRule("<INI>","<PORTÓN>","portón");
            newRule("<PORTÓN>","<PIEZA>","box");
            newRule("<INI>","<AMBAS>","ambas");
            newRule("<AMBAS>","<PIEZA>","puertas","aletas");
            newRule("<INI>","<PILAR>","pilar");
            newRule("<PILAR>","<PIEZA>","a","b","c");

            newRule("<PIEZA>","<GRID>","grid");
            newRule("<GRID>", "<NUM>", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho");
            newRule("<PIEZA>","<NUM>","");

//        newRule("<PIEZA>","<END>","completo","defecto","añade bollo","añade mancha");
            newRule("<NUM>","<END>","completo","defecto");
//        newRule("<PIEZA>","<AÑADE>","añade");
            newRule("<NUM>","<AÑADE>","añade");
            newRule("<AÑADE>","<END>","bollo","mancha");
        }
    }

}
