package com.chelz.libraries.navigation.cicerone

interface Command

data class Forward(val screen: Screen) : Command

data class Replace(val screen: Screen) : Command

class Back : Command

data class BackTo(val screen: Screen?) : Command