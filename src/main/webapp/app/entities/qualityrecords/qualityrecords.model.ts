export interface IQualityrecords {
  id: number;
  supplier?: string | null;
  test2?: number | null;
}

export type NewQualityrecords = Omit<IQualityrecords, 'id'> & { id: null };
