import groovy.json.*

@NonCPS
create(json){
  def team = json.riglet_info.name
  def mailcount = json.config.emails.email.size()
	print(mailcount)
   
  def jsonBuilder = new groovy.json.JsonBuilder()
  
  def jsonSlurper = new JsonSlurper()
  def reader = new BufferedReader(new InputStreamReader(new FileInputStream("/var/lib/jenkins/workspace/${JOB_NAME}/jenkins.json"),"UTF-8"))
  def jsonObj = jsonSlurper.parse(reader)
  List<String> LIST = new ArrayList<String>();
  List<String> LIST2 = new ArrayList<String>();
  //def jsonObj = readJSON text: metrics
  print(team)
  int score=0;
  def teamS=jsonObj.JENKINS.teamsuccessbuild_cnt
  if(teamS>10){
  score+=10;
  LIST.add(["metric":"Team Successfull Builds","Value":score,"Tool":"JENKINS"])
  }
  def teamF=jsonObj.JENKINS.teamfailurebuild_cnt
  if(teamF<5){
  score+=10;
  LIST.add(["metric":"Team Failure Builds","Value":score,"Tool":"JENKINS"])
  }
  def indCount=jsonObj.JENKINS.individualsuccess.size()
  for(j=0;j<mailcount;j++)
   {
    def email=json.config.emails.email[j] 
	   print(email)
     for(k=0;k<indCount;k++){
       def mail=jsonObj.JENKINS.individualsuccess[k].email
       if(email.equals(mail)){
         def countS=jsonObj.JENKINS.individualsuccess[k].Success_cnt
         if(countS>2){
           score+=10;
           LIST2.add(["email":mail,"metric":"Successfull Builds","Value":score,"Tool":"JENKINS"])
         }
       }
     }
   }
  
  
  
  jsonBuilder(
    "Teamname":team,
    "Metrics" : LIST,
    "Metrics" : LIST2
    
    
    )
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/JenkinsScore.json")
  file.write(jsonBuilder.toPrettyString())
}


def call(jsondata){
def jsonString = jsondata
def json = readJSON text: jsonString

  create(json)
}
