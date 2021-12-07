export interface IMarcaPc {
  id?: number;
  nombreMarca?: string | null;
}

export class MarcaPc implements IMarcaPc {
  constructor(public id?: number, public nombreMarca?: string | null) {}
}

export function getMarcaPcIdentifier(marcaPc: IMarcaPc): number | undefined {
  return marcaPc.id;
}
