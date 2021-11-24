package edu.temple.audiobb
import java.io.Serializable

data class Book (val id : Int, val title : String, val author : String,  val cover_url : String, val duration: Int): Serializable
