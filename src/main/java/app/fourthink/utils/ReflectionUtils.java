/**
 * 
 */
package app.fourthink.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Helio_Medeiros
 *
 */
public class ReflectionUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getParamType(Object o){
		Class<T> classe = null;
		Type type = o.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] arguments = ((ParameterizedType)type).getActualTypeArguments();
			for (Type argument : arguments){ 
				if (argument instanceof Class) {
					classe = (Class<T>) argument;								
				}
			}
		}
		return classe;
	}
	
	public static <T> T instantiate(Class<T> classe) {
		try{
			return (T) classe.newInstance();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
