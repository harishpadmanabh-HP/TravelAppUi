package com.harish.travelappui.sequenceshapes


var defaultCircleRadius = 2
var defaultSquareLength = 2
var defaultRectLength = 2
var defaultRectBreadth = 2

var shapeHistory = ArrayList<String>()
var sequenceHistory = ArrayList<String>()

    fun main(args: Array<String>) {

        getSequence()


        println("Do you want to continue\n(y/n)")
        val choice = readLine()
        if(choice?.equals("y")==true)
            getSequence()
        else
            showHistory()

    }

fun showHistory() {
    println("Sequence History")
    sequenceHistory.forEach { sequence->
        println(sequence)
    }
    println("\n\n Shape History")
    shapeHistory.forEach { shape->
        println(shape)
    }
}

fun getSequence() {

    print("Enter sequence")

    val enteredSequence = readLine()
    println("Sequence Entered: $enteredSequence")
    if (enteredSequence != null) {
        sequenceHistory.add(enteredSequence)
    }

    val sequence = enteredSequence?.toCharArray()
    sequence?.forEachIndexed { index,shape->
        when(shape){
            'c'->{
                drawCircle(defaultCircleRadius,index)
            }
            's'->{
                drawSquare(defaultSquareLength,index)
            }
            'r'->{
                drawRectangle(defaultRectLength, defaultRectBreadth , index)
            }
        }
    }}

fun drawRectangle(defaultRectLength: Int, defaultRectBreadth: Int, index: Int) {
    var length = defaultRectLength
    var breadth = defaultRectBreadth
    println("Drawing rectangle........")
    println("Change Default size (y/n)")
    val choice = readLine()
    if(choice.equals("y"))
    {
        println("Enter length")
        length = readLine()?.toInt() ?: defaultRectLength
        breadth = readLine()?.toInt() ?: defaultRectBreadth
    }
    var finalLength = (index+1)*length
    var finalBreadth = (index+1)*breadth

    val result = "Drawn Shape \n RECTANGLE    l=$finalLength    b=$finalBreadth\n.................."
    shapeHistory.add(result)
    println(result)



}


fun drawSquare(defaultSquareLength: Int, index: Int) {
    var length = defaultSquareLength
    println("Drawing square........")
    println("Change Default size (y/n)")
    val choice = readLine()
    if(choice.equals("y"))
    {
        println("Enter length")
        length = readLine()?.toInt() ?: defaultSquareLength
    }
    val finalLength = (index+1)*length
    val result = "Drawn Shape \n SQUARE     r=$finalLength \n........................"
    shapeHistory.add(result)
    println(result)



}

fun drawCircle(defaultCircleRadius: Int, index: Int) {
    var radius = defaultCircleRadius
    println("Drawing circle........")
    println("Change Default size (y/n)")
    val choice = readLine()
    if(choice.equals("y"))
    {
        println("Enter radius")
        radius = readLine()?.toInt() ?: defaultCircleRadius
    }
    var finalRadius = (index+1)*radius
    val result = "Drawn Shape \n CIRCLE     r=$finalRadius \n..........................."
    shapeHistory.add(result)
    println(result)



}

