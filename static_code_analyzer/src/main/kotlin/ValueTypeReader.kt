import ast_node.BinaryTokenNode
import ast_node.Value
import token.TokenType

class ValueTypeReader {
    fun getValueType(value: BinaryTokenNode): TokenType{
        var valueType: TokenType = TokenType.NUMBER_KEYWORD
        fun dfs(value: BinaryTokenNode){
            if (isLeaf(value)){
                if (value.token.type != TokenType.NUMBER_KEYWORD){
                    valueType = TokenType.STRING_KEYWORD
                    return
                }
            }else{
                dfs(value.left!!)
                dfs(value.right!!)
            }
        }
        dfs(value)
        return  valueType
    }

    fun isLeaf(binaryTokenNode: BinaryTokenNode): Boolean{
        return binaryTokenNode.left == null && binaryTokenNode.right == null
    }
}