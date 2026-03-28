export type Genre= "Comic" | "Horror" | "Romance";

export interface Book{
    readonly id:number,
    title: string,
    author: string,
    genre: Genre,
    year: number,
    pages: number,
    rating: number,
    available: boolean,
    description?: string
}