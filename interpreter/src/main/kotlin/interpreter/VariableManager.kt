package interpreter

class VariableManager{
    private val variables: MutableMap<String, Variable> = HashMap()
    private val mutableVariable: MutableMap<String, Boolean> = HashMap()
    private val conditionVariables: MutableMap<String, Variable> = HashMap()
    private val mutableConditionVariable: MutableMap<String, Boolean> = HashMap()
    private var inCondition: Boolean = false

    fun add (name: String, value: Variable, isMutable: Boolean) {
        if (!variables.containsKey(name)){
            if (inCondition){
                if (!conditionVariables.containsKey(name)){
                    conditionVariables[name] = value
                    mutableConditionVariable[name] = isMutable
                }
            }else{
                variables[name] = value
                mutableVariable[name] = isMutable
            }
        }
    }

    fun replace (name: String, value: Variable){
        if (variables.containsKey(name)){
            variables.replace(name, value)
            return
        }
        if (conditionVariables.containsKey(name)){
            conditionVariables.replace(name, value)
            return
        }else{
            throw Exception("$name not found")
        }
    }

    fun get(name: String): Variable{
        if (variables.containsKey(name)){
            return variables[name]!!
        }
        if (conditionVariables.containsKey(name)){
            return conditionVariables[name]!!
        }else{
            throw Exception("$name not found")
        }
    }

    fun contains(name: String): Boolean {
        return if (inCondition){
            conditionVariables.contains(name)
        }else{
            variables.containsKey(name)
        }
    }

    fun isMutable(name: String): Boolean{
        if(mutableVariable.contains(name)){
            return mutableVariable[name]!!
        }
        if (mutableConditionVariable.contains(name)){
            return mutableConditionVariable[name]!!
        }else{
            return false
        }
    }

    fun setInCondition(condition: Boolean){
        inCondition = if (condition){
            true
        }else{
            conditionVariables.clear()
            mutableConditionVariable.clear()
            false
        }
    }
}

class Variable(private val value: String?, private val type: ValueType){
    fun getValue(): String?{
        return value
    }
    fun getType(): ValueType{
        return type
    }
}

enum class ValueType{
    NUMBER,
    STRING,
    BOOLEAN,
    INPUT,
    ERROR
}