//  Operator.java
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package momfo.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lib.math.BuiltInRandom;
import momfo.util.JMException;

/**
 * Class representing an operator
 */
public abstract class Operator implements Serializable {


  protected final Map<String , Object> parameters_;

  protected BuiltInRandom random;

  public void setRandomGenerator(BuiltInRandom ran) {
	  random = ran;
  }

  public Operator(HashMap<String , Object> parameters) {
	parameters_ = parameters;
  }

  public Map<String,Object> getMap(){
	  return parameters_;
  }

  public String name;

  public String getName(){
	  return name;
  }


  abstract public Object execute(Object object) throws JMException ;


  public void setParameter(String name, Object value) {
    parameters_.put(name, value);
  } // setParameter

  public Object getParameter(String name) {
    return parameters_.get(name);
  } //getParameter

} // Operator
