package com.github.ldaniels528.trifecta.io.avro

import java.io._

import com.github.ldaniels528.trifecta.io.{KeyAndMessage, MessageOutputSource}
import com.github.ldaniels528.trifecta.messages.MessageDecoder
import com.github.ldaniels528.commons.helpers.OptionHelper.Risky._

import scala.concurrent.ExecutionContext

/**
  * Avro File Output Source
  * @author lawrence.daniels@gmail.com
  */
class AvroFileMessageOutputSource(out: OutputStream) extends MessageOutputSource {
  private var dos_? : Option[DataOutputStream] = None

  override def open() = dos_? = new DataOutputStream(out)

  override def write(data: KeyAndMessage, decoder: Option[MessageDecoder[_]])(implicit ec: ExecutionContext) = {
    dos_? foreach { out =>
      // persist the key
      out.writeInt(data.key.length)
      out.write(data.key)

      // persist the message
      out.writeInt(data.message.length)
      out.write(data.message)
    }
  }

  override def close() = dos_? foreach { out =>
    out.flush()
    out.close()
  }

}

/**
  * File Output Handler Singleton
  * @author lawrence.daniels@gmail.com
  */
object AvroFileMessageOutputSource {

  def apply(path: String): AvroFileMessageOutputSource = {
    new AvroFileMessageOutputSource(new FileOutputStream(path))
  }

  def apply(file: File): AvroFileMessageOutputSource = {
    new AvroFileMessageOutputSource(new FileOutputStream(file))
  }

}
