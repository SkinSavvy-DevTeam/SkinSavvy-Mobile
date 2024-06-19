package com.myapp.skinsavvy

import com.google.gson.annotations.SerializedName

data class ResponseSolution(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Level(

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class Data(

	@field:SerializedName("severityLevelSolution")
	val severityLevelSolution: SeverityLevelSolution? = null
)

data class Solution(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("explanation")
	val explanation: String? = null
)

data class SeverityLevelSolution(

	@field:SerializedName("solution")
	val solution: Solution? = null,

	@field:SerializedName("level")
	val level: Level? = null
)
