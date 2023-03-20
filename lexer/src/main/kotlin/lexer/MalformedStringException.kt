package lexer

import utilities.Location

class MalformedStringException(message: String, location: Location) : Exception(message + " at " +  location.row + ", " + location.column)
