import * as dayjs from 'dayjs';
import { IMarcaPc } from 'app/entities/marca-pc/marca-pc.model';

export interface IRespuesta {
  id?: number;
  numeroIdentificacion?: number | null;
  email?: string | null;
  comentarios?: string | null;
  fechaHora?: dayjs.Dayjs | null;
  marcaPc?: IMarcaPc | null;
}

export class Respuesta implements IRespuesta {
  constructor(
    public id?: number,
    public numeroIdentificacion?: number | null,
    public email?: string | null,
    public comentarios?: string | null,
    public fechaHora?: dayjs.Dayjs | null,
    public marcaPc?: IMarcaPc | null
  ) {}
}

export function getRespuestaIdentifier(respuesta: IRespuesta): number | undefined {
  return respuesta.id;
}
