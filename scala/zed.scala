
object zed {
  val exampleHostnames = Seq(
      "analytics-test-kafkabroker-4.uswest1-2.exampleapp.com"
    , "search-prod-esdata-23.uswest2-1.exampleapp.com"
    , "messaging-staging-api-6.eucentral1-1.exampleapp.com"
    , "web-integ-proxy-3.euwest1-2.exampleapp.com"
    , "api-prod-cache-6.euwest1-1.exampleapp.com"
  )

  sealed trait Service
  object Service {
    final case object Analytics extends Service
    final case object Search extends Service
    final case object Messaging extends Service
    final case object Api extends Service
    final case object Web extends Service

    def analytics: Service = Analytics
    def search: Service = Search
    def messaging: Service = Messaging
    def api: Service = Api
    def web: Service = Web

    def apply(s: String): Option[Service] = s.toLowerCase match {
      case "analytics"  => Some(Analytics)
      case "search"     => Some(Search)
      case "messaging"  => Some(Messaging)
      case "api"        => Some(Api)
      case "web"        => Some(Web)
      case _            => None
    }
  }

  sealed trait Environment
  object Environment {
    final case object Integ extends Environment
    final case object Test extends Environment
    final case object Staging extends Environment
    final case object Prod extends Environment

    def integ: Environment = Integ
    def test: Environment = Test
    def staging: Environment = Staging
    def prod: Environment = Prod

    def apply(s: String): Option[Environment] = s.toLowerCase match {
      case "integ"    => Some(Integ)
      case "test"     => Some(Test)
      case "staging"  => Some(Staging)
      case "prod"     => Some(Prod)
      case _          => None
    }
  }

  sealed trait Role
  object Role {
    final case object KafkaBroker extends Role
    final case object EsData extends Role
    final case object Api extends Role
    final case object Proxy extends Role
    final case object Cache extends Role
    // unused currently in examples
    final case object HBase extends Role
    final case object Postgres extends Role

    def apply(s: String): Option[Role] = s.toLowerCase match {
      case "kafkabroker"  => Some(KafkaBroker)
      case "esdata"       => Some(EsData)
      case "api"          => Some(Api)
      case "proxy"        => Some(Proxy)
      case "cache"        => Some(Cache)
      case "hbase"        => Some(HBase)
      case "postgres"     => Some(Postgres)
      case _              => None
    }
  }

  sealed trait Pod
  object Pod {
    final case object Pod1 extends Pod
    final case object Pod2 extends Pod

    def pod1: Pod = Pod1
    def pod2: Pod = Pod2

    def apply(s: String): Option[Pod] = s match {
      case "1"  => Some(Pod1)
      case "2"  => Some(Pod2)
      case _    => None
    }
  }

  sealed trait Datacenter
  object Datacenter {
    final case object UsWest1 extends Datacenter
    final case object UsWest2 extends Datacenter
    final case object EuWest1 extends Datacenter
    final case object EuCentral1 extends Datacenter

    def uswest1: Datacenter     = UsWest1
    def uswest2: Datacenter     = UsWest2
    def euwest1: Datacenter     = EuWest1
    def eucentral1: Datacenter  = EuCentral1

    def apply(s: String): Option[Datacenter] = s.toLowerCase match {
      case "uswest1"    => Some(UsWest1)
      case "uswest2"    => Some(UsWest2)
      case "euwest1"    => Some(EuWest1)
      case "eucentral1" => Some(EuCentral1)
      case _            => None
    }

    def unapply(d: Datacenter): String = d match {
      case UsWest1    => "uswest1"
      case UsWest2    => "uswest2"
      case EuWest1    => "euwest1"
      case EuCentral1 => "eucentral1"
    }
  }
}
