
def saveQueryResults(query, results):
    with open("QuerysResults.txt", "a") as file_object:
        file_object.write(createXmlString(query, results))
  
    
def createXmlString(query,results):
    import xml.etree.ElementTree as ET
    
    newQuery = ET.Element("query", name = query)
    newResults = ET.SubElement(newQuery, "results", name="results")
    for result in results:
        ET.SubElement(newResults, "result",name=result)
       
    return (ET.tostring(newQuery).decode("utf-8")+"\n")

for i in range(10):
    query='ohm'+str(i)
    results=['a'+str(i),'b'+str(i),'c'+str(i),'d'+str(i),'e'+str(i)]
    saveQueryResults(query, results)