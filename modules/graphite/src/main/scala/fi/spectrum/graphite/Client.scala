package fi.spectrum.graphite

import cats.effect.Resource
import com.comcast.ip4s.{IpAddress, SocketAddress}
import fs2.Chunk
import fs2.io.net.{Datagram, DatagramSocket, Network}
import tofu.higherKind.RepresentableK

import java.net.InetSocketAddress

trait Client[F[_]] {

  def send(message: Array[Byte]): F[Unit]
}

object Client {

  implicit val representableK: RepresentableK[Client] =
    tofu.higherKind.derived.genRepresentableK

  def make[F[_]: Network](
    settings: GraphiteSettings
  ): Resource[F, Client[F]] =
    for {
      remote <- Resource.pure(new InetSocketAddress(settings.host, settings.port))
      socket <- Network[F].openDatagramSocket()
    } yield new UdpClient[F](SocketAddress.fromInetSocketAddress(remote), socket)

  final class UdpClient[F[_]](
    remote: SocketAddress[IpAddress],
    socket: DatagramSocket[F]
  ) extends Client[F] {

    def send(message: Array[Byte]): F[Unit] =
      socket.write(Datagram(remote, Chunk.array(message)))
  }
}
