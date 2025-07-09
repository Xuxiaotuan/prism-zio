package cn.xuyinyin.prism

import com.coralogix.zio.k8s.client.model.K8sNamespace
import com.coralogix.zio.k8s.client.v1.pods._
import com.coralogix.zio.k8s.model.core.v1.Pod
import com.coralogix.zio.k8s.model.pkg.apis.meta.v1.{DeleteOptions, ObjectMeta}
import zio._
import zio.test._

object PodsSpec extends ZIOSpecDefault {

  /**
   * 一个测试用的 Pod 对象，metadata 里带了名字 "test-pod"
   */
  val samplePod: Pod = Pod(
    metadata = Some(ObjectMeta(name = Some("test-pod"))),
    spec = None,
    status = None
  )

  /**
   * 创建一个 Pods 的测试实现
   * Pods.test 会返回一个“假的”Pods环境，不会真的调用 Kubernetes API
   * 每次 create/delete 返回 samplePod
   */
  val testLayer: ULayer[Pods] = Pods.test(() => samplePod)

  /**
   * Spec是 zio-test 的测试套件
   */
  def spec: Spec[TestEnvironment with Scope, Any] = suite("Pods Service")(
    /**
     * 第一个测试：创建和获取 Pod
     */
    test("should create and get Pod successfully") {
      for {
        // 创建 Pod
        // ZIO.serviceWithZIO 从 ZIO 环境里取出 Pods 实现，调用 create 方法
        created <- ZIO.serviceWithZIO[Pods](
          _.create(
            newResource = samplePod,
            namespace = K8sNamespace("default")
          )
        )

        // 输出日志
        _ <- Console.printLine(s"Created pod: ${created.metadata.flatMap(_.name)}")

        // 获取 Pod
        fetched <- ZIO.serviceWithZIO[Pods](
          _.get(
            name = "test-pod",
            namespace = K8sNamespace("default")
          )
        )

        // 输出日志
        _ <- Console.printLine(s"Fetched pod: ${fetched.metadata.flatMap(_.name)}")
      } yield
      // 断言：
      // 创建和获取到的 Pod 名字都是 "test-pod"
      assertTrue(
        created.metadata.flatMap(_.name).contains("test-pod"),
        fetched.metadata.flatMap(_.name).contains("test-pod")
      )
    },
    /**
     * 第二个测试：删除 Pod
     */
    test("should delete Pod successfully") {
      for {
        // 调用 delete 删除 Pod
        deleted <- ZIO.serviceWithZIO[Pods](
          _.delete(
            name = "test-pod",
            deleteOptions = DeleteOptions(),
            namespace = K8sNamespace("default")
          )
        )
      } yield
      // 断言：返回的 deleted Pod 名字也是 "test-pod"
      assertTrue(
        deleted.metadata.flatMap(_.name).contains("test-pod")
      )
    }
  )
    // 提供 Pods 测试实现
    .provideLayer(testLayer)
}
