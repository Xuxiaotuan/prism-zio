package cn.xuyinyin.prism

import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.v1.pods._
import com.coralogix.zio.k8s.model.core.v1.Pod
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.{DeleteOptions, ObjectMeta}
import zio._
import zio.test._

object PodsSpec extends ZIOSpecDefault {

  val samplePod: Pod = Pod(
    metadata = Some(ObjectMeta(name = Some("test-pod"))),
    spec = None,
    status = None
  )

  val testLayer: ULayer[Pods] = Pods.test(() => samplePod)

  def spec: Spec[TestEnvironment with Scope, Object] = suite("Pods Service")(
    test("should create and get Pod successfully") {
      for {
        created <- ZIO.serviceWithZIO[Pods](_.create(samplePod, K8sNamespace("default")))
        _       <- Console.printLine(s"Created pod: ${created.metadata.flatMap(_.name)}")

        fetched <- ZIO.serviceWithZIO[Pods](_.get("test-pod", K8sNamespace("default")))
        _       <- Console.printLine(s"Fetched pod: ${fetched.metadata.flatMap(_.name)}")
      } yield assertTrue(
        created.metadata.flatMap(_.name).contains("test-pod"),
        fetched.metadata.flatMap(_.name).contains("test-pod")
      )
    },
    test("should delete Pod successfully") {
      for {
        deleted <- ZIO.serviceWithZIO[Pods](
          _.delete(
            name = "test-pod",
            deleteOptions = DeleteOptions(),
            namespace = K8sNamespace("default")
          )
        )
      } yield assertTrue(
        deleted.metadata.flatMap(_.name).contains("test-pod")
      )
    }
  ).provideLayer(testLayer)
}
